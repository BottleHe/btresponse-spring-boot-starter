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

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

    static {
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public BtMappingJackson2HttpMessageConverter() {
        this(OBJECT_MAPPER);
    }

    public BtMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
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
