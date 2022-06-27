package work.bottle.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import work.bottle.demo.domain.AdministrationRegion;

@Mapper
public interface AdministrationRegionMapper {

    public int insert(AdministrationRegion region);
}
