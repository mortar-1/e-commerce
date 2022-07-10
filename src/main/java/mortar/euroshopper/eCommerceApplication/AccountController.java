package mortar.euroshopper.eCommerceApplication;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login(Model model) {

        accountService.addAttributesToModelForPageLogin(model);

        return "login";
    }

    @GetMapping("/accounts")
    public String viewAccounts(Model model) {

        accountService.addAttributesToModelForPageAccounts(model);

        return "accounts";
    }

    @GetMapping("/account")
    public String viewCurrentAccount(Model model, @ModelAttribute NewUpdateAccount newUpdateAccount, @ModelAttribute NewUpdatePassword newUpdatePassword) {

        accountService.addAttributesToModelForPageCurrentAccount(model);

        return "currentAccount";
    }
    
    @GetMapping("/accounts/{id}")
    public String viewAccount(Model model, @PathVariable Long id, @ModelAttribute NewUpdateAccount newUpdateAccount, @ModelAttribute NewPasswordCheck newPasswordCheck) {
        
        accountService.addAttributesToModelForPageAccount(model,id);
        
        return "account";
    }

    @GetMapping("/accounts/new")
    public String viewRegisterForn(Model model, @ModelAttribute NewAccount newAccount) {

        accountService.addAttributesToModelForPageRegisterForm(model);

        return "register-form";
    }

    @PostMapping("/accounts/new")
    public String addNewAccount(Model model, @Valid @ModelAttribute NewAccount newAccount, BindingResult bindingResult, @RequestParam(defaultValue = "false") Boolean privacyPolicyIsRead) {

        if (accountService.hasErrorsOnCreation(newAccount, bindingResult)) {

            accountService.addAttributesToModelForPageRegisterForm(model);

            return "register-form";
        }

        accountService.newAccount(newAccount);

        return "redirect:/login?created=true";
    }

    @PostMapping("/account/update")
    public String updateCurrentAccount(Model model, @Valid @ModelAttribute NewUpdateAccount ua, BindingResult br, @ModelAttribute NewUpdatePassword nup) {

        if (accountService.hasErrorsOnCurrentAccountUpdate(ua, br)) {

            accountService.addAttributesToModelForPageCurrentAccount(model);

            return "currentAccount";
        }

        accountService.updateAccount(ua);

        return "redirect:/account?updated=true";
    }

    @PostMapping("/account/password")
    public String updateCurrentAccountPassword(Model model, @Valid @ModelAttribute NewUpdatePassword nup, BindingResult br, @ModelAttribute NewUpdateAccount ua) {

        if (accountService.hasErrorsOnPasswordUpdate(nup, br)) {

            accountService.addAttributesToModelForPageCurrentAccount(model);

            return "currentAccount";
        }

        accountService.updatePassword(nup);

        return "redirect:/account?passwordUpdated=true";
    }

    @PostMapping("/accounts/{id}/update")
    public String updateAccount(Model model, @PathVariable Long id, @Valid @ModelAttribute NewUpdateAccount newUpdateAccount, BindingResult br) {

        if (accountService.hasErrorsOnAccountUpdate(newUpdateAccount, br, id)) {

            accountService.addAttributesToModelForPageAccount(model, id);

            return "account";
        }

        accountService.updateAccount(newUpdateAccount, id);

        return "redirect:/accounts/" + id + "?updated=true";
    }

    @PostMapping("/accounts/{id}/delete")
    public String deleteAccount(Model model, @PathVariable Long id, @Valid @ModelAttribute NewPasswordCheck newPasswordCheck, BindingResult br, @ModelAttribute NewUpdateAccount newUpdateAccount ) {
        
        if (accountService.hasErrorsOnAccountDeletion(newPasswordCheck, br)) {
            
            accountService.addAttributesToModelForPageAccount(model, id);
            
            return "account";
        }
        

        accountService.deleteAccount(id);

        return "redirect:/accounts?deleted=true";
    }

    @PostMapping("/account/delete")
    public String deleteCurrentAccount() {

        accountService.deleteAccount();

        return "redirect:/shop?deleted=true";
    }

    @PostConstruct
    public void atStart() {

        accountService.createDefaultAccounts();
    }
}
