package com.whatachad.app.service;

import com.whatachad.app.common.BError;
import com.whatachad.app.common.CommonException;
import com.whatachad.app.model.domain.Facility;
import com.whatachad.app.model.domain.User;
import com.whatachad.app.model.request.CreateFacilityRequestDto;
import com.whatachad.app.model.request.FacilityDto;
import com.whatachad.app.model.request.UpdateFacilityRequestDto;
import com.whatachad.app.model.response.FacilityResponseDto;
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

    private final FacilityMapperService mapperService;
    private final UserService userService;
    private final FacilityRepository facilityRepository;

    @Transactional
    public Facility createFacility(CreateFacilityRequestDto dto) {
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
    public void updateFacility(UpdateFacilityRequestDto dto) {
        Facility findFacility = facilityRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonException(BError.NOT_EXIST, "Facility"));
        if (!findFacility.getUser().equals(getLoginUser())) {
            throw new CommonException(BError.NOT_MATCH, "User");
        }
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
        return userService.getUser(loginUserId);
    }
}
