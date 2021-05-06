package com.picklerick.schedule.rest.api.controller;

import com.picklerick.schedule.rest.api.model.User;
import com.picklerick.schedule.rest.api.repository.UserRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }


    /**
     * Returns a list with all users
     * only an Admin user has access to all users
     * @author Clelia
     **/
    @ModelAttribute("users")
    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    Iterable<User> all(Model model){
        model.addAttribute("users", repository.findAll());
        return repository.findAll();
    }

    /**
     * Returns a user with a specific id
     * @author: Clelia
     * @param id the id of the user to retrieve
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.userId == #id")
    @GetMapping("/users/{id}")
    User one(@PathVariable Long id, Authentication authentication) throws Exception {
        return repository.findById(id).orElseThrow(()-> new Exception());
    }

    /**
     * Update a users information
     * @author: Clelia & Stefan
     *
     * @param id id of user updating their information
     */
    @Secured("ROLE_ADMIN")
    @PatchMapping("/users/{id}")
    public User updateUserAsAdmin(@RequestBody Map<String, Object> userUpdates, @PathVariable Long id) {
        User user = repository.findById(id).get();
        // Fetch User data from db and
        // go through all the possible options of change
        // and save the changes to the user
        return repository.findById(id)
                .map(u -> {
                    userUpdates.forEach(
                            (update, value)-> {
                                switch (update) {
                                    case "firstname":
                                        u.setFirstname((String) value);
                                        break;
                                    case "lastname":
                                        u.setLastname((String) value);
                                        break;
                                    case "email":
                                        u.setEmail((String) value);
                                        break;
                                    case "weekly_schedule":
                                        u.setWeekly_schedule((Double.parseDouble((String) value)));
                                        break;
                                    case "manager_id":
                                        u.setManager_id((Long.parseLong((String) value)));
                                        break;
                                    // TODO solve role issue + create such a method for normal users
                                }
                            }
                    );
                    return repository.save(u);
                }).orElseGet(() -> repository.save(user));

    }


    /**
     * Get all users created by admin
     * @authors Clelia,
     *
     * @param id admin-id
     */
    @GetMapping("/users/admin/{id}")
    Iterable<User> allByAdmin(@PathVariable Long id) {
        //TODO find all users created by admin
        return repository.findAll();
    }

    /**
     * Create new user
     * @author Clelia
     * */
    @Secured("ROLE_ADMIN")
    @PostMapping("/users")
    public User addNewUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    //------------------------- Not in scope yet ----------------------------
    /**
     * Change setting of a user
     * @author Clelia
     *
     * @param id user id
     * */

    //TODO does not update email, weekly_schedule, manager_id or is_admin, why?

  /*
  @Secured("ROLE_ADMIN")
  @PatchMapping("/users/{id}")
    public User changeUserData(@PathVariable Long id, @RequestBody Map<String, Object> userUpdates){
        // get saved user as fallback option
        User user = repository.findById(id).get();
        // Fetch User data from db and
        // go through all the possible options of change
        // and save the changes to the user
        return repository.findById(id)
                .map(u -> {
                    userUpdates.forEach(
                            (update, value) -> {
                                switch (update){
                                    case "firstname": u.setFirstname((String) value); break;
                                    case "lastname": u.setLastname((String) value); break;
                                    case "email": u.setEmail((String) value); break;
                                    case "weekly_schedule": u.setWeekly_schedule((Double) value); break;
                                    case "manager_id": u.setManager_id((Long) value); break;
                                    case "is_admin": u.setIs_admin((Boolean) value); break;
                                }
                            });
                    return repository.save(u);
                })
                .orElseGet(()-> repository.save(user));
    }
    This is for test purposes
       @PostMapping("/user")
    public User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }
    @PatchMapping("/user/{id}/checkin")
    public User checkinUser(@RequestBody User user, @PathVariable Long id) {
        return repository.findById(id)
        .map(u ->{
            user.setCheckinDate(new Date());
            return repository.save(u);
        })
                .orElseGet(() -> repository.save(user));
    }*/

}