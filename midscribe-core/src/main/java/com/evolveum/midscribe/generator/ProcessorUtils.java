package com.evolveum.midscribe.generator;

import com.evolveum.midpoint.prism.*;
import com.evolveum.midpoint.schema.constants.ObjectTypes;
import com.evolveum.midpoint.schema.result.OperationResult;
import com.evolveum.midpoint.util.LocalizableMessage;
import com.evolveum.midpoint.util.exception.SchemaException;
import com.evolveum.midpoint.xml.ns._public.common.common_3.*;
import com.evolveum.midscribe.generator.store.GetOptions;
import com.evolveum.midscribe.generator.store.ObjectStore;
import com.evolveum.prism.xml.ns._public.types_3.RawType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

/**
 * Created by Viliam Repan (lazyman).
 */
public class ProcessorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorUtils.class);

    private final GeneratorContext context;

    private Properties properties = new Properties();

    public ProcessorUtils(GeneratorContext context) {
        this.context = context;

        URL url = ProcessorUtils.class.getResource("/midscribe.properties");
        try (InputStream is = url.openStream()) {
            properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOG.error("Couldn't load midscribe.properties from classpath", ex);
        }

        GeneratorOptions opts = context.getConfiguration();
        if (opts.getProperties() != null) {
            properties.putAll(opts.getProperties());
        }
    }

    private PrismSerializer<String> getSerializer(PrismContext prismContext) {
        return prismContext.xmlSerializer()
                .options(SerializationOptions.createSerializeReferenceNames());
    }

    public String serialize(Object object) throws SchemaException {
        if (object == null) {
            return null;
        }

        if (object instanceof RawType) {
            return ((RawType) object).extractString();
        }

        PrismContext prismContext = context.getPrismContext();

        final QName fakeQName = new QName(PrismConstants.NS_TYPES, "object");

        PrismSerializer<String> serializer = getSerializer(prismContext);

        String result;
        if (object instanceof ObjectType) {
            ObjectType ot = (ObjectType) object;
            result = serializer.serialize(ot.asPrismObject());
        } else if (object instanceof PrismObject) {
            result = serializer.serialize((PrismObject<?>) object);
        } else if (object instanceof OperationResult) {
            Function<LocalizableMessage, String> resolveKeys = msg -> msg.getFallbackMessage();
            OperationResultType operationResultType = ((OperationResult) object).createOperationResultType(resolveKeys);
            result = serializer.serializeAnyData(operationResultType, fakeQName);
        } else {
            result = serializer.serializeAnyData(object, fakeQName);
        }

        return result;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String translator(String placeholder) {
        if (placeholder != null && placeholder.startsWith("$(") && placeholder.endsWith(")")) {
            String key = placeholder.substring(2, placeholder.length() - 1);
            String value = properties.getProperty(key);
            if (value != null){
                return value;
            }
        } else {
            return placeholder;
        }
        return placeholder;
    }

    public List<UserType> loadUsers() throws Exception {
        return loadObjects(UserType.class);
    }

    public List<ArchetypeType> loadArchetypes() throws Exception {
        return loadObjects(ArchetypeType.class);
    }

    public List<ValuePolicyType> loadValuePolicies() throws Exception {
        return loadObjects(ValuePolicyType.class);
    }

    public List<SecurityPolicyType> loadSecurityPolicies() throws Exception {
        return loadObjects(SecurityPolicyType.class);
    }

    public List<SystemConfigurationType> loadSystemConfiguration() throws Exception {
        return loadObjects(SystemConfigurationType.class);
    }

    public List<LookupTableType> loadLookupTables() throws Exception {
        return loadObjects(LookupTableType.class);
    }

    public List<RoleType> loadRoles() throws Exception {
        return loadObjects(RoleType.class);
    }

    public List<OrgType> loadOrgs() throws Exception {
        return loadObjects(OrgType.class);
    }

    public List<ObjectTemplateType> loadObjectTemplates() throws Exception {
        return loadObjects(ObjectTemplateType.class);
    }

    public List<TaskType> loadTasks() throws Exception {
        return loadObjects(TaskType.class);
    }

    public List<ResourceType> loadResources() throws Exception {
        List<ResourceType> resources = loadObjects(ResourceType.class);

        for (ResourceType resource : resources) {
            if (resource.getSchemaHandling() == null) {
                continue;
            }

            SchemaHandlingType schemaHandling = resource.getSchemaHandling();
            // todo sort
//            Collections.sort(schemaHandling.getObjectType(), (o1, o2) -> {
//
//                o1.getDisplayName();
//                o1.getKind();
//                o1.getIntent();
//
//                o1.getDisplayName();
//                o1.getKind();
//                o1.getIntent();
//            });
        }

        return resources;
    }

    public List<FunctionLibraryType> loadFunctionLibraries() throws Exception {
        return loadObjects(FunctionLibraryType.class);
    }

    public <T extends ObjectType> T getObject(ObjectReferenceType ref) {
        if (ref == null) {
            return null;
        }

        QName type = ref.getType();
        if (type == null) {
            type = ObjectType.COMPLEX_TYPE;
        }

        if (ref.getOid() == null) {
            return null;
        }

        return getObject(type, ref.getOid());
    }

    public <T extends ObjectType> T getObject(QName type, String oid) {
        return getObject(ObjectTypes.getObjectTypeClass(type), oid);
    }

    public <T extends ObjectType> T getObject(Class<T> type, String oid) {
        if (type == null || StringUtils.isEmpty(oid)) {
            return null;
        }

        ObjectStore store = context.getStore();
        return store.get(type, oid, GetOptions.create());
    }

    private <T extends ObjectType> List<T> loadObjects(Class<T> type) throws Exception {
        List<T> objects = new ArrayList<>();

        ObjectStore store = context.getStore();
        List<T> result = store.list(type, GetOptions.create());

        objects.addAll(result);
        Collections.sort(objects, new ObjectTypeComparator());

        return objects;
    }

    private static class ObjectTypeComparator implements Comparator<ObjectType> {

        @Override
        public int compare(ObjectType o1, ObjectType o2) {
            String s1 = getName(o1);
            String s2 = getName(o2);

            return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
        }

        private String getName(ObjectType o) {
            if (o == null) {
                return null;
            }

            return TemplateUtils.getOrig(o.getName());
        }
    }
}
