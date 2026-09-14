package work.bottle.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * 自定义类型转换器, 在强制模式下将其优先级提升到最高.
 */
public class BtMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(BtMappingJackson2HttpMessageConverter.class);

    public BtMappingJackson2HttpMessageConverter() {
        this(buildDefaultObjectMapper());
    }

    public BtMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    /**
     * 容器中没有ObjectMapper时(未引入jackson的自动配置)使用的兜底配置.
     */
    public static ObjectMapper buildDefaultObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
    }

    /**
     * 强制模式下约定: 任何返回值都要能被序列化, 空Bean序列化为{}, 不抛出异常.
     * 在保留使用方Jackson定制(模块/日期格式/命名策略等)的前提下, 关闭FAIL_ON_EMPTY_BEANS.
     */
    public static ObjectMapper enforceForceWritable(ObjectMapper objectMapper) {
        return objectMapper.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                ? objectMapper.copy().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                : objectMapper;
    }

    // 这里不处理请求参数, 只处理返回值
    @Override
    protected boolean canRead(MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {

        logger.debug("BtMappingJackson2HttpMessageConverter::canWrite({}, {})", clazz, mediaType);

        // return Object.class != clazz ? super.canWrite(clazz, mediaType) : true;
        return true;
    }
}
