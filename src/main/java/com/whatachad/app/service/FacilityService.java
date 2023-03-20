package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.request.CreateFacilityDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityDto;
import com.whatachad.app.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacilityService {

    private final MapperService mapperService;
    private final UserService userService;
    private final FacilityRepository facilityRepository;

    @Transactional
    public Facility createFacility(CreateFacilityDto dto) {
        FacilityDto facilityDto = mapperService.toFacilityDto(dto);
        return facilityRepository.save(Facility.create(getLoginUser(), facilityDto));
    }

    @Transactional(readOnly = true)
    public List<Facility> findAllFacilities() {
        return facilityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Facility findFacility(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Facility"));
    }

    @Transactional
    public void updateFacility(Long id, UpdateFacilityDto dto) {
        Facility findFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Facility"));
        FacilityDto facilityDto = mapperService.toFacilityDto(dto);
        findFacility.update(facilityDto);
    }

    @Transactional
    public void deleteFacility(Long id) {
        Facility findFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Facility"));
        facilityRepository.delete(findFacility);
    }

    private User getLoginUser() {
        String loginUserId = userService.getLoginUserId();
        User user = userService.getUser(loginUserId);
        return user;
    }
}
