package work.bottle.demo.service;

import work.bottle.demo.domain.HotelInfo;
import work.bottle.demo.model.query.HotelQuery;

import java.util.List;

public interface HotelService {
    public int insertMultiHotelData(List<HotelInfo> list);

    public int getHotelInfoCount(HotelInfo hotelInfo);

    public List<HotelInfo> getHotelInfoList(HotelQuery query);
}
