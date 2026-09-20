
package com.mycompany.nexachat;

public class Register {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public boolean checkUserName() {
        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) {
                hasCapital = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapital && hasNumber && hasSpecialCharacter;
    }

    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null
                && cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted.";
        }

        return "Registration successful.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null
                && password != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + " " + lastName
                    + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

