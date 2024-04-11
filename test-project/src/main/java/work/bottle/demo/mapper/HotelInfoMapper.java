package work.bottle.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import work.bottle.demo.domain.HotelInfo;
import work.bottle.demo.model.Query;

import java.util.List;

@Mapper
public interface HotelInfoMapper {
    public int insertMultiHotelData(@Param("hotelInfoList") List<HotelInfo> hotelInfoList);

    public int getHotelInfoCount(HotelInfo hotelInfo);

    public List<HotelInfo> getHotelInfoList(Query<HotelInfo> query);
}
