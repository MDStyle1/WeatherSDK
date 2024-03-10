package com.mds.weather.mapper;

import com.mds.weather.dto.MainApiDto;
import com.mds.weather.model.MainResult;

public interface MainMapper<W extends MainResult, D extends MainApiDto> {

    Class<D> getClassDto();

    W createFromApiDto(D dto);

}
