package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;


public class AccountService {
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO AccountDAO){
        this.accountDAO = AccountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
        
    }
    private boolean izGood(Account account) {
        return !account.getUsername().isEmpty() && account.getPassword().length() >= 4;
    }

    private boolean doesExist(Account account) throws ClassNotFoundException {
        return accountDAO.getAccountByUsername(account.getUsername()) != null;
    }

    public Account registerUser(Account account) throws ClassNotFoundException {      
        if (izGood(account) && !doesExist(account)) {
            return accountDAO.userRegistration(account);
        } else
        return null;        
        
    }
    public Account loginAccount(Account account) throws ClassNotFoundException {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }



}