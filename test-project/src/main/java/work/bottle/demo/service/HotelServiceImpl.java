package work.bottle.demo.service;

import org.springframework.stereotype.Service;
import work.bottle.demo.domain.HotelInfo;
import work.bottle.demo.mapper.HotelInfoMapper;
import work.bottle.demo.model.query.HotelQuery;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService{

    private final HotelInfoMapper hotelInfoMapper;

    public HotelServiceImpl(HotelInfoMapper hotelInfoMapper) {
        this.hotelInfoMapper = hotelInfoMapper;
    }

    @Override
    public int insertMultiHotelData(List<HotelInfo> list) {
        return hotelInfoMapper.insertMultiHotelData(list);
    }

    @Override
    public int getHotelInfoCount(HotelInfo hotelInfo) {
        return hotelInfoMapper.getHotelInfoCount(hotelInfo);
    }

    @Override
    public List<HotelInfo> getHotelInfoList(HotelQuery query) {
        return hotelInfoMapper.getHotelInfoList(query);
    }
}
