package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.dto.FacilityDto;
import com.whatachad.app.model.request.FindFacilityDto;
import com.whatachad.app.repository.FacilityRepository;
import com.whatachad.app.type.FacilityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FacilityService {

    private final UserService userService;
    private final FacilityRepository facilityRepository;

    @Transactional
    public Facility createFacility(FacilityDto facilityDto) {
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

    @Transactional(readOnly = true)
    public Slice<Facility> findFacilitiesAroundV1(Pageable pageable, FindFacilityDto findFacilityDto) {
        return facilityRepository.findFacilitiesAroundUser(pageable, findFacilityDto);
    }

    @Transactional(readOnly = true)
    public Slice<Facility> findFacilitiesAroundV2(Pageable pageable, FacilityType category, String[] regionCodes) {
        return facilityRepository.findFacilitiesAroundUserV2(pageable, category, regionCodes);
    }

    @Transactional(readOnly = true)
    public Slice<Facility> findFacilitiesInArea(Pageable pageable, String area) {
        return facilityRepository.findByArea(pageable, area);
    }

    @Transactional
    public void updateFacility(FacilityDto facilityDto) {
        Facility findFacility = facilityRepository.findById(facilityDto.getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Facility"));
        if (!findFacility.getUser().equals(getLoginUser())) {
            throw new CommonException(BError.NOT_MATCH, "User");
        }
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
        return userService.getUser(loginUserId);
    }
}
