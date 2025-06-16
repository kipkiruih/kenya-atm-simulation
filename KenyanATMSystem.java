import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KenyanATMSystem {
    // Account database simulation
    private static Map<String, Account> accounts = new HashMap<>();
    // ATM cash balance
    private static double atmBalance = 500000.0; // KES 500,000
    
    public static void main(String[] args) {
        initializeAccounts();
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Karibu (Welcome) to Equity Bank ATM");
        System.out.println("Please insert your card (Press Enter to continue)...");
        scanner.nextLine();
        
        // Card verification
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();
        
        System.out.print("Enter your PIN: ");
        String pin = scanner.nextLine();
        
        Account currentAccount = authenticate(accountNumber, pin);
        if (currentAccount == null) {
            System.out.println("Invalid account number or PIN. Kwaheri (Goodbye).");
            return;
        }
        
        System.out.println("\nHabari, " + currentAccount.getCustomerName() + "!");
        
        boolean sessionActive = true;
        while (sessionActive) {
            displayMenu();
            System.out.print("Select an option (1-5): ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    checkBalance(currentAccount);
                    break;
                case 2:
                    withdrawCash(currentAccount, scanner);
                    break;
                case 3:
                    depositCash(currentAccount, scanner);
                    break;
                case 4:
                    transferMoney(currentAccount, scanner);
                    break;
                case 5:
                    System.out.println("Asante (Thank you) for banking with us. Kwaheri!");
                    sessionActive = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
    
    private static void initializeAccounts() {
        // Sample accounts with Kenyan names and locations
        accounts.put("123456789", new Account("123456789", "1234", "John Kamau", 150000.0, "Nairobi"));
        accounts.put("987654321", new Account("987654321", "4321", "Mary Wanjiku", 75000.0, "Mombasa"));
        accounts.put("456789123", new Account("456789123", "7890", "James Otieno", 25000.0, "Kisumu"));
    }
    
    private static Account authenticate(String accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.getPin().equals(pin)) {
            return account;
        }
        return null;
    }
    
    private static void displayMenu() {
        System.out.println("\n===== ATM Menu =====");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Deposit Cash");
        System.out.println("4. Transfer Money");
        System.out.println("5. Exit");
    }
    
    private static void checkBalance(Account account) {
        System.out.printf("Your current balance is: KES %,.2f%n", account.getBalance());
        System.out.println("Account Location: " + account.getLocation());
    }
    
    private static void withdrawCash(Account account, Scanner scanner) {
        System.out.print("Enter amount to withdraw (KES): ");
        double amount = scanner.nextDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        if (amount > account.getBalance()) {
            System.out.println("Insufficient funds in your account.");
            return;
        }
        
        if (amount > atmBalance) {
            System.out.println("ATM has insufficient cash. Please try a lower amount.");
            return;
        }
        
        if (amount > 40000) {
            System.out.println("Maximum withdrawal limit is KES 40,000 per transaction.");
            return;
        }
        
        // Kenyan ATM denominations
        int[] denominations = {1000, 500, 200, 100, 50};
        int[] notes = new int[denominations.length];
        double remaining = amount;
        
        System.out.println("Dispensing:");
        for (int i = 0; i < denominations.length; i++) {
            if (remaining >= denominations[i]) {
                notes[i] = (int)(remaining / denominations[i]);
                remaining = remaining % denominations[i];
                System.out.println(denominations[i] + " KES notes: " + notes[i]);
            }
        }
        
        account.withdraw(amount);
        atmBalance -= amount;
        System.out.printf("Withdrawal successful. New balance: KES %,.2f%n", account.getBalance());
    }
    
    private static void depositCash(Account account, Scanner scanner) {
        System.out.print("Enter amount to deposit (KES): ");
        double amount = scanner.nextDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        // Kenyan ATMs typically accept up to 40 notes per deposit
        System.out.println("Please insert your cash (maximum 40 notes)");
        // Simulate processing time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        account.deposit(amount);
        atmBalance += amount;
        System.out.printf("Deposit successful. New balance: KES %,.2f%n", account.getBalance());
    }
    
    private static void transferMoney(Account account, Scanner scanner) {
        System.out.print("Enter recipient account number: ");
        scanner.nextLine(); // Consume newline
        String recipientNumber = scanner.nextLine();
        
        System.out.print("Enter amount to transfer (KES): ");
        double amount = scanner.nextDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }
        
        if (amount > account.getBalance()) {
            System.out.println("Insufficient funds in your account.");
            return;
        }
        
        Account recipient = accounts.get(recipientNumber);
        if (recipient == null) {
            System.out.println("Recipient account not found.");
            return;
        }
        
        // Confirm transfer
        System.out.printf("Transfer KES %,.2f to %s? (Y/N): ", amount, recipient.getCustomerName());
        String confirm = scanner.next();
        
        if (confirm.equalsIgnoreCase("Y")) {
            account.withdraw(amount);
            recipient.deposit(amount);
            System.out.println("Transfer successful.");
            System.out.printf("Your new balance: KES %,.2f%n", account.getBalance());
        } else {
            System.out.println("Transfer cancelled.");
        }
    }
}

class Account {
    private String accountNumber;
    private String pin;
    private String customerName;
    private double balance;
    private String location;
    
    public Account(String accountNumber, String pin, String customerName, double balance, String location) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.customerName = customerName;
        this.balance = balance;
        this.location = location;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getPin() {
        return pin;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void deposit(double amount) {
        balance += amount;
    }
    
    public void withdraw(double amount) {
        balance -= amount;
    }
}
