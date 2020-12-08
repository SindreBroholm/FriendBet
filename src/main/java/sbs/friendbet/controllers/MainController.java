package sbs.friendbet.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sbs.friendbet.data.*;
import sbs.friendbet.repositories.BetCollectorRepo;
import sbs.friendbet.repositories.BetRepo;
import sbs.friendbet.repositories.FriendTrackerRepo;
import sbs.friendbet.repositories.UserRepo;
import sbs.friendbet.repositories.validators.BetValidator;
import sbs.friendbet.repositories.validators.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private UserRepo userRepo;
    private BetRepo betRepo;
    private BetCollectorRepo betCollectorRepo;
    private PasswordEncoder passwordEncoder;
    private FriendTrackerRepo friendTrackerRepo;

    public MainController(UserRepo userRepo, BetRepo betRepo, BetCollectorRepo betCollectorRepo,
                          PasswordEncoder passwordEncoder, FriendTrackerRepo friendTrackerRepo) {
        this.userRepo = userRepo;
        this.betRepo = betRepo;
        this.betCollectorRepo = betCollectorRepo;
        this.passwordEncoder = passwordEncoder;
        this.friendTrackerRepo = friendTrackerRepo;
    }

    @GetMapping("/login")
    public String showLoginView() {
        return "Login";
    }

    @GetMapping("/signup")
    public String showSignupView(Principal principal, Model model) {
        if (isUserLoggedIn(principal)) {
            return "redirect:/home";
        } else {
            model.addAttribute("user", new User());
            return "Signup";
        }
    }

    @PostMapping("/signup")
    public String creatNewUser(@ModelAttribute User user, BindingResult br) {
        if (userClassValidationSuccesses(user, br)) {
            if (br.hasErrors()) {
                return "Signup";
            } else {
                saveUser(user);
                return "Login";
            }
        } else {
            return "Login";
        }
    }

    //make home a notification stream
    @GetMapping("/home")
    public String showHomeView(Principal principal, Model model) {
        User user = getLoggedInUser(principal);

        model.addAttribute("listOfFriends", friendTrackerRepo.findAllFriends(user.getId()));
        model.addAttribute("bet", new Bet());
        return "Home";
    }

    @GetMapping("/friends/{toggle}")
    public String showFriendsList(Principal principal, Model model, @PathVariable boolean toggle){
        User user = getLoggedInUser(principal);
        model.addAttribute("CurrentUser", user);
        model.addAttribute("toggle", toggle);
        if (!toggle){
            model.addAttribute("listOfFriends", friendTrackerRepo.findAllFriends(user.getId()));
        } else {
            model.addAttribute("pendingFriendRequest", friendTrackerRepo.findPendingFriendRequest(user.getId()));
        }
        return "Friends";
    }

    @GetMapping("/friends/{toggle}/{friendshipId}/{response}")
    public String friendHandling(@PathVariable boolean toggle, @PathVariable String friendshipId, @PathVariable boolean response){
        FriendTracker ft = friendTrackerRepo.findByFriendshipId(friendshipId);
        if (response){
            ft.setPending(false);
            friendTrackerRepo.save(ft);
        } else {
            friendTrackerRepo.delete(ft);
        }
        return "redirect:/friends/" + toggle;
    }
    @GetMapping("/friendRequest/{friendId}")
    public String sendFriendRequest(@PathVariable int friendId, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        User friend = userRepo.findById(friendId);
        friendTrackerRepo.save(new FriendTracker(user, friend));
        return "Search";
    }


    @GetMapping("/friends/search")
    public String searchForFriends(Principal principal, Model model){
        model.addAttribute("user", userRepo.findByUsername(principal.getName()));
        model.addAttribute("friendTracker", new FriendTracker());
        return "Search";
    }
    @PostMapping("/friends/search")
    public String search(Model model, @RequestParam() String keyword) {
        List<User> searchResults = null;
        if (keyword != null) {
            searchResults = userRepo.searchForFriendByName(keyword);
        }
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        return "Search";
    }

    @GetMapping("/bet/{username}")
    public String showBetView(Model model, Principal principal, @PathVariable String username){
        User user = getLoggedInUser(principal);
        User friend = userRepo.findByUsername(username);


     /*   model.addAttribute("friendList", friendTrackerRepo.findAllByUserIdOrFriendId(user.getId(), user.getId()));*/
        model.addAttribute("bet", new Bet());
        return "Bet";
    }

    @PostMapping("/bet/{username}")
    public String placeBet(@ModelAttribute Bet bet, BindingResult br, @PathVariable String username, Principal principal) {
        if (betClassValidationSuccesses(bet, br)) {
            if (br.hasErrors()) {
                return "Bet";
            } else{
                saveBet(bet, listOfUsersToBetAgainst(username), principal);
            }
        }
        return "Home";
    }

    private List<User> listOfUsersToBetAgainst(String userIds) {
        List<User> betAgainstThisUsers = new ArrayList<>();
        String[] userIdsSplit = userIds.split(",");
        for (String username: userIdsSplit) {
            betAgainstThisUsers.add(userRepo.findByUsername(username));
        }
        return betAgainstThisUsers;
    }
    private void saveBet(Bet bet, List<User> betAgainstThisUsers, Principal principal) {
        betRepo.save(bet);
        for (User againstUser : betAgainstThisUsers) {
            betCollectorRepo.save(new BetCollector(getLoggedInUser(principal), bet, againstUser));
        }
    }
    private boolean betClassValidationSuccesses(@Valid Bet bet, BindingResult br) {
        BetValidator betValidator = new BetValidator();
        if (betValidator.supports(bet.getClass())) {
            betValidator.validate(bet, br);
            return true;
        } else {
            return false;
        }
    }
    private User getLoggedInUser(Principal principal){
        return userRepo.findByUsername(principal.getName());
    }
    private boolean isUserLoggedIn(Principal principal) {
        return principal != null;
    }
    private boolean userClassValidationSuccesses(@Valid User user, BindingResult br) {
        UserValidator userValidator = new UserValidator(userRepo);
        if (userValidator.supports(user.getClass())) {
            userValidator.validate(user, br);
            return true;
        } else {
            return false;
        }
    }
    private void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        userRepo.save(user);
    }
}
