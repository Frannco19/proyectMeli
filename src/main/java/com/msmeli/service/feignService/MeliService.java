package com.msmeli.service.feignService;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.msmeli.feignClient.MeliClient;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeliService {

    private final MeliClient meliClient;

    public MeliService(MeliClient meliClient) {
        this.meliClient = meliClient;
    }

    public List<Object> getProductSearch(String query, String site) {
        DocumentContext json = JsonPath.parse(meliClient.getProductSearch(query, site));
        JSONArray results = json.read("$.results[*]");

        return results.stream()
                .map(JsonPath::parse)
                .map(productContext -> new JSONObject() {{
                    put("title", productContext.read("$.title"));
                    put("price", productContext.read("$.price"));
                    put("seller_nickname", productContext.read("$.seller.nickname"));
                }})
                .collect(Collectors.toList());
    }
}
