package com.whatachad.app.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class ReverseGeocodeDto {

    private List<Document> documents;

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Document {
        private RoadAddress road_address;
        private Address address;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class RoadAddress {
        private String address_name;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    public static class Address {
        private String address_name;
    }
}
