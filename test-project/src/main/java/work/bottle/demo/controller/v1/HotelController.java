package work.bottle.demo.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import work.bottle.demo.domain.HotelInfo;
import work.bottle.demo.model.SimpleList;
import work.bottle.demo.model.query.HotelQuery;
import work.bottle.demo.service.HotelService;

import java.util.List;

@RequestMapping("/hotel/v1")
@RestController
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/list")
    public SimpleList<HotelInfo> getHotelList(HotelQuery query) {
        int cnt = hotelService.getHotelInfoCount(query.getData());
        if (0 >= cnt) {
            return new SimpleList<>();
        }
        return new SimpleList<>(cnt, hotelService.getHotelInfoList(query));
    }

    @GetMapping("/{id}")
    public HotelInfo getHotelInfoById(@PathVariable("id") Integer id) {
        HotelInfo hotelInfo = new HotelInfo();
        hotelInfo.setId(id);
        HotelQuery query = new HotelQuery(hotelInfo);
        query.setPage(1);
        query.setPageCnt(1);

        List<HotelInfo> infoList = hotelService.getHotelInfoList(query);
        if (null != infoList && infoList.size() > 0) {
            return infoList.get(0);
        }
        return null;
    }

}
