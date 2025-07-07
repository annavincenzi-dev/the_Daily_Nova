package dev.annavincenzi.the_daily_nova.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.annavincenzi.the_daily_nova.models.CareerRequest;
import dev.annavincenzi.the_daily_nova.models.Role;
import dev.annavincenzi.the_daily_nova.models.User;
import dev.annavincenzi.the_daily_nova.repositories.CareerRequestRepository;
import dev.annavincenzi.the_daily_nova.repositories.RoleRepository;
import dev.annavincenzi.the_daily_nova.repositories.UserRepository;

@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        CareerRequest request = careerRequestRepository.findById(requestId).get();

        User user = request.getUser();
        Role role = request.getRole();

        List<Role> rolesUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        rolesUser.add(newRole);

        user.setRoles(rolesUser);
        userRepository.save(user);

        user.setRoles(rolesUser);
        userRepository.save(user);
        request.setIsChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(user.getEmail(), "Career request accepted",
                "Your request for " + newRole.getName() + " has been accepted");

    }

    @Override
    public CareerRequest find(Long id) {
        return careerRequestRepository.findById(id).get();
    }

    @Override
    public void careerReject(Long requestId) {
        CareerRequest request = careerRequestRepository.findById(requestId).get();
        request.setIsChecked(true);
        request.setIsRejected(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail(request.getUser().getEmail(), "Career request rejected",
                "Your request for " + request.getRole().getName() + " has been rejected");
    }

}
