package ru.practicum.explorewithme.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.LocationDto;
import ru.practicum.explorewithme.main.model.Location;

@Component
public class LocationMapper {
    public LocationDto toDto(Location loc) {
        if (loc == null) {
            return null;
        }
        return LocationDto.builder()
                .lat(loc.getLat())
                .lon(loc.getLon())
                .build();
    }

    public Location fromDto(LocationDto dto) {
        if (dto == null) return null;
        return new Location(dto.getLat(), dto.getLon());
    }
}