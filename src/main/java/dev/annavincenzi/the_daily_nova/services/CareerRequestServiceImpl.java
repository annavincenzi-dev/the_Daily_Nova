package dev.annavincenzi.the_daily_nova.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.annavincenzi.the_daily_nova.models.CareerRequest;
import dev.annavincenzi.the_daily_nova.models.User;
import dev.annavincenzi.the_daily_nova.repositories.CareerRequestRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest) {
        List<Long> allUsersIds = careerRequestRepository.findAllUsersIds();

        if (!allUsersIds.contains(user.getId())) {
            return false;
        }

        List<Long> requests = careerRequestRepository.findUserById(user.getId());

        return requests.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }

    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequestRepository.save(careerRequest);

        emailService.sendSimpleEmail("admin@dailynova.com",
                "New career request for " + careerRequest.getRole().getName(), " from user: " + user.getUsername());

    }

    @Override
    public void careerAccept(Long requestId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'careerAccept'");
    }

    @Override
    public CareerRequest find(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'find'");
    }

}
