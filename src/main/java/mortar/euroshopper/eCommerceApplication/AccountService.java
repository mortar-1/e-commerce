package mortar.euroshopper.eCommerceApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    public Authentication getCurrentAuthentication() {

        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Account getCurrentAccount() {

        return accountRepository.findByEmail(getCurrentAuthentication().getName());
    }

    public String getCurrentAccountEmail() {

        return getCurrentAccount().getEmail();
    }

    public Long getCurrentAccountId() {

        return getCurrentAccount().getId();
    }

    public String getAccountEmail(Long id) {

        return accountRepository.getById(id).getEmail();
    }

    public void addCurrentAccountToModel(Model model) {

        model.addAttribute("currentAccount", getCurrentAccount());
    }

    public void addAttributesToModelForPageRegisterForm(Model model) {

    }

    public void addAttributesToModelForPageLogin(Model model) {

        addCurrentAccountToModel(model);
    }

    public void addAttributesToModelForPageCurrentAccount(Model model) {

        addCurrentAccountToModel(model);

        model.addAttribute("account", accountRepository.getById(getCurrentAccountId()));
    }

    public void addAttributesToModelForPageAccount(Model model, Long id) {

        addCurrentAccountToModel(model);

        model.addAttribute("account", accountRepository.getById(id));
    }

    public void addAttributesToModelForPageAccounts(Model model) {

        addCurrentAccountToModel(model);

        model.addAttribute("accounts", accountRepository.findAll());
    }

    public Boolean hasErrorsOnCreation(NewAccount na, BindingResult br) {

        if (accountRepository.findByEmail(na.getEmail()) != null) {

            br.rejectValue("email", "error.newAccount", "an account with this email allready exists");
        }

        if (!na.getPassword().equals(na.getConfirmPassword())) {

            br.rejectValue("confirmPassword", "error.newAccount", "passwords don't match");
        }

        if (na.getPrivacyPolicyIsRead() == null) {

            br.rejectValue("privacyPolicyIsRead", "error.newAccount", "privacy policy must be read");
        }

        return br.hasErrors();
    }

    public Boolean hasErrorsOnCurrentAccountUpdate(NewUpdateAccount ua, BindingResult br) {

        if (accountRepository.findByEmail(ua.getEmail()) != null && !getCurrentAccountEmail().equals(ua.getEmail())) {

            br.rejectValue("email", "error.newUpdateAccount", "an account with this email allready exists");
        }

        if (!ua.getPhoneNumber().matches("[0-9]+|\\+[0-9]+")) {

            br.rejectValue("phoneNumber", "error.newUpdateAccount", "phone number may contain only digits and the character \"+\"");
        }

        if (!passwordEncoder.matches(ua.getCurrentPassword(), getCurrentAccount().getPassword())) {

            br.rejectValue("currentPassword", "error.newDeleteAccount", "wrong password");
        }

        return br.hasErrors();
    }

    public Boolean hasErrorsOnAccountUpdate(NewUpdateAccount ua, BindingResult br, Long id) {

        if (accountRepository.findByEmail(ua.getEmail()) != null && !getAccountEmail(id).equals(ua.getEmail())) {

            br.rejectValue("email", "error.newUpdateAccount", "an account with this email allready exists");
        }

        if (!ua.getPhoneNumber().matches("[0-9]+|\\+[0-9]+")) {

            br.rejectValue("phoneNumber", "error.newUpdateAccount", "phone number may contain only digits and the character \"+\"");
        }

        if (!passwordEncoder.matches(ua.getAdminPassword(), getCurrentAccount().getPassword())) {

            br.rejectValue("adminPassword", "error.newDeleteAccount", "wrong password");
        }

        return br.hasErrors();
    }

    public Boolean hasErrorsOnPasswordUpdate(NewUpdatePassword newPw, BindingResult br) {

        if (!passwordEncoder.matches(newPw.getCurrentPassword(), getCurrentAccount().getPassword())) {

            br.rejectValue("currentPassword", "error.newDeleteAccount", "wrong password");
        }

        if (!newPw.getNewPassword().equals(newPw.getConfirmNewPassword())) {

            br.rejectValue("confirmPassword", "error.newAccount", "passwords don't match");
        }

        return br.hasErrors();
    }

    public Boolean hasErrorsOnAccountDeletion(NewPasswordCheck npc, BindingResult br) {

        if (!passwordEncoder.matches(npc.getPassword(), getCurrentAccount().getPassword())) {

            br.rejectValue("password", "error.newPasswordCheck", "wrong password");
        }

        return br.hasErrors();
    }

    public void updateAccount(NewUpdateAccount ua) {

        Account ac = getCurrentAccount();

        ac.setEmail(ua.getEmail());

        ac.setFirstName(ua.getFirstName());

        ac.setLastName(ua.getLastName());

        ac.setAddress(ua.getAddress());

        ac.setPostalCode(ua.getPostalCode());

        ac.setCity(ua.getCity());

        ac.setCountry(ua.getCountry());

        ac.setPhoneNumber(ua.getPhoneNumber());

        accountRepository.save(ac);
    }

    public void updateAccount(NewUpdateAccount ua, Long id) {

        Account ac = accountRepository.getById(id);

        ac.setEmail(ua.getEmail());

        ac.setFirstName(ua.getFirstName());

        ac.setLastName(ua.getLastName());

        ac.setAddress(ua.getAddress());

        ac.setPostalCode(ua.getPostalCode());

        ac.setCity(ua.getCity());

        ac.setCountry(ua.getCountry());

        ac.setPhoneNumber(ua.getPhoneNumber());

        accountRepository.save(ac);
    }

    public void updatePassword(NewUpdatePassword newPw) {

        Account ac = getCurrentAccount();

        ac.setPassword(passwordEncoder.encode(newPw.getNewPassword()));

        accountRepository.save(ac);
    }

    public void newAccount(NewAccount newAccount) {

        Boolean privacyPolicyIsRead = false;

        if (newAccount.getPrivacyPolicyIsRead().equals("on")) {

            privacyPolicyIsRead = true;
        }

        accountRepository.save(new Account(
                newAccount.getEmail(),
                newAccount.getPassword(),
                newAccount.getFirstName(),
                newAccount.getLastName(),
                newAccount.getAddress(),
                newAccount.getPostalCode(),
                newAccount.getCity(),
                newAccount.getCountry(),
                newAccount.getPhoneNumber(),
                new ArrayList<>(Arrays.asList("USER")),
                LocalDateTime.now(),
                privacyPolicyIsRead));
    }

    public void deleteAccount() {

        accountRepository.delete(getCurrentAccount());
    }

    public void deleteAccount(Long id) {

        accountRepository.deleteById(id);
    }

    public void createDefaultAccounts() {

        createAdminAndCustomerForDevelopment();

        createBoss();
    }

    public void createBoss() {

        if (accountRepository.findByEmail("boss") == null) {

            Account account = new Account();

            account.setEmail("boss");

            account.setPassword(passwordEncoder.encode("123eCommerce!"));

            account.setRoles(new ArrayList<>(Arrays.asList("USER", "ADMIN", "BOSS")));

            accountRepository.save(account);
        }
    }

    public void createAdminAndCustomerForDevelopment() {

        if (accountRepository.findAll().isEmpty() && Arrays.asList(env.getActiveProfiles()).contains("dev")) {

            List<String> roles = new ArrayList<>(Arrays.asList("USER"));

            String password = passwordEncoder.encode("123eCommerce!");

            accountRepository.save(new Account(
                    "johndoe@customermail.com",
                    password,
                    "Jon",
                    "Doe",
                    "Customer Street 15",
                    "05810",
                    "Customer City",
                    "Customerland",
                    "+358401234567",
                    roles,
                    LocalDateTime.now(),
                    true));

            roles.add("ADMIN");

            accountRepository.save(new Account(
                    "janedoe@yourcompany.com",
                    password,
                    "Jane",
                    "Doe",
                    "Admin Street 15",
                    "05810",
                    "Admin City",
                    "Adminland",
                    "+358501234567",
                    roles,
                    LocalDateTime.now(),
                    true));
        }
    }
}
