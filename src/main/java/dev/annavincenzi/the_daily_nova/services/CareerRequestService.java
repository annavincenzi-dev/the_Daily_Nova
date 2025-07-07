package dev.annavincenzi.the_daily_nova.services;

import dev.annavincenzi.the_daily_nova.models.CareerRequest;
import dev.annavincenzi.the_daily_nova.models.User;

public interface CareerRequestService {
    boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest);

    void save(CareerRequest careerRequest, User user);

    void careerAccept(Long requestId);

    void careerReject(Long requestId);

    CareerRequest find(Long id);
}
