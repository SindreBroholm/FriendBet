package sbs.friendbet.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sbs.friendbet.data.*;
import sbs.friendbet.repositories.*;
import sbs.friendbet.repositories.validators.BetValidator;
import sbs.friendbet.repositories.validators.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final UserRepo userRepo;
    private final BetRepo betRepo;
    private final BetCollectorRepo betCollectorRepo;
    private final PasswordEncoder passwordEncoder;
    private final FriendTrackerRepo friendTrackerRepo;
    private final NotificationRepo notificationRepo;

    public MainController(UserRepo userRepo, BetRepo betRepo, BetCollectorRepo betCollectorRepo,
                          PasswordEncoder passwordEncoder, FriendTrackerRepo friendTrackerRepo, NotificationRepo notificationRepo) {
        this.userRepo = userRepo;
        this.betRepo = betRepo;
        this.betCollectorRepo = betCollectorRepo;
        this.passwordEncoder = passwordEncoder;
        this.friendTrackerRepo = friendTrackerRepo;
        this.notificationRepo = notificationRepo;
    }

    @GetMapping("/login")
    public String showLoginView(Principal principal) {
        if (isUserLoggedIn(principal)) {
            return "redirect:/home";
        }
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
        return saveUserIfValid(user, br);
    }

    @GetMapping("/home")
    public String showHomeView(Principal principal, Model model) {
        User user = getLoggedInUser(principal);
        model.addAttribute("CurrentUser", user);
        model.addAttribute("listOfFriends", friendTrackerRepo.findAllFriends(user.getId()));
        getNotifications(model, user);
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
        getNotifications(model, user);
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

        FriendTracker exist = friendTrackerRepo.findByFriendshipId(getFriendshipId(user, friend));
        if (exist == null){
            friendTrackerRepo.save(new FriendTracker(user, friend));
        }
        return "Search";
    }

    @GetMapping("/friends/search")
    public String searchForFriends(Principal principal, Model model){
        model.addAttribute("user", userRepo.findByUsername(principal.getName()));
        model.addAttribute("friendTracker", new FriendTracker());
        getNotifications(model, getLoggedInUser(principal));
        return "Search";
    }
    @PostMapping("/friends/search")
    public String search(Model model, @RequestParam() String keyword, Principal principal) {
        model.addAttribute("user", userRepo.findByUsername(principal.getName()));
        model.addAttribute("searchResults", getSearchResults(keyword));
        model.addAttribute("keyword", keyword);
        getNotifications(model, getLoggedInUser(principal));
        return "Search";
    }

    @GetMapping("/bet/{friendId}")
    public String showBetView(Model model, Principal principal, @PathVariable int friendId){
        model.addAttribute("friend", userRepo.findById(friendId));
        model.addAttribute("user", getLoggedInUser(principal));
        model.addAttribute("bet", new Bet());
        getNotifications(model, getLoggedInUser(principal));
        return "Bet";
    }

    @PostMapping("/bet/{friendId}")
    public String placeBet(@ModelAttribute Bet bet, BindingResult br, @PathVariable int friendId, Principal principal, Model model) {
        User friend = userRepo.findById(friendId);
        model.addAttribute("friend", friend);
        getNotifications(model, getLoggedInUser(principal));
        return saveBetIfValid(bet, br, principal, friend);
    }

    @GetMapping("/bets")
    public String showBets(Model model, Principal principal){
        User user = getLoggedInUser(principal);
        model.addAttribute("challengesAgainstFriends", betCollectorRepo.findAllByUserId(user.getId()));
        model.addAttribute("challengesFromFriends", betCollectorRepo.findAllByAgainstUser(user.getId()));
        getNotifications(model, user);
        return "Bets";
    }

    private String saveUserIfValid(User user, BindingResult br) {
        if (userClassValidationSuccesses(user, br)) {
            return saveUserIfFormIsValid(user, br);
        } else {
            return "Login";
        }
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
    private String saveUserIfFormIsValid(User user, BindingResult br) {
        if (br.hasErrors()) {
            return "Signup";
        } else {
            saveUser(user);
            return "Login";
        }
    }
    private void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
        userRepo.save(user);
    }
    private List<User> getSearchResults(String keyword) {
        List<User> searchResults = null;
        if (keyword != null) {
            searchResults = userRepo.searchForFriendByName(keyword);
        }
        return searchResults;
    }

    private String saveBetIfValid(Bet bet, BindingResult br, Principal principal, User friend) {
        if (betClassValidationSuccesses(bet, br)) {
            String Bet = saveBetIfFormIsValid(bet, br, principal, friend);
            if (Bet != null) return Bet;
        }
        return "redirect:/bets";
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
    private String saveBetIfFormIsValid(Bet bet, BindingResult br, Principal principal, User friend) {
        if (br.hasErrors()) {
            return "Bet";
        } else{
            saveBet(bet, friend, principal);
        }
        return null;
    }
    private void saveBet(Bet bet, User friend, Principal principal) {
        betRepo.save(bet);
        betCollectorRepo.save(new BetCollector(getLoggedInUser(principal), bet, friend));

    }

    private String getFriendshipId(User user, User friend) {
        String ft;
        if (user.getId() > friend.getId()){
            ft = friend.getId() + "_" + user.getId();
        } else {
            ft = user.getId() + "_" + friend.getId();
        }
        return ft;
    }

    private boolean isUserLoggedIn(Principal principal) {
        return principal != null;
    }
    private User getLoggedInUser(Principal principal){
        return userRepo.findByUsername(principal.getName());
    }
    private void getNotifications(Model model, User user) {
        model.addAttribute("notificationStream", notificationRepo.findAllByRecipientId(user.getId()));
    }
}
