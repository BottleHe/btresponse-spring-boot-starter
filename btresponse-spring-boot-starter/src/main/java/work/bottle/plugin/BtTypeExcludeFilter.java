package work.bottle.plugin;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;

/**
 * 只能过滤Components Scan中的Bean, 不采用
 */
@Deprecated
public class BtTypeExcludeFilter extends TypeExcludeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        System.out.println(metadataReader.getClassMetadata().getClassName());
        return super.match(metadataReader, metadataReaderFactory);
    }
}
