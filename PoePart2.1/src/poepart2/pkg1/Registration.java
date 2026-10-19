/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart2.pkg1;

public class Registration {

    String userName;
    String passWord;
    String cellPhone;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        return password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*\\d.*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    public boolean checkCellPhoneNumber(String cellphone) {
        return cellphone.matches("^\\+27[0-9]{9}$")
                || cellphone.matches("^0[0-9]{9}$");
    }

    public String registerUser(String username, String password, String cellphone) {
        if (checkUserName(username)
                && checkPasswordComplexity(password)
                && checkCellPhoneNumber(cellphone)) {
            this.userName = username;
            this.passWord = password;
            this.cellPhone = cellphone;
            return "registration was a success";
        } else {
            return "registration was unsuccessful";
        }
    }

    public boolean registerStatus(String username, String password, String cellphone) {
        return checkUserName(username)
                && checkPasswordComplexity(password)
                && checkCellPhoneNumber(cellphone);
    }
}