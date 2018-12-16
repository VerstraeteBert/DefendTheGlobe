package data.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {
    public JSONObject formValidationRegistration(String email, String username, String password) {
        JSONObject message = new JSONObject();
        ArrayList<String> passwordErrors = validatePassword(password);
        ArrayList<String> usernameErrors = validateUsername(username);


        String emailError = "";
        if (!validateEmail(email)){
            emailError = "Email is not valid";
        }

        if (passwordErrors.isEmpty() && usernameErrors.isEmpty() && emailError.equals("")){
           message.put("message", "success");

        } else {
            ArrayList<String> errors = new ArrayList<>();


            message.put("message", "failure");
            if (!passwordErrors.isEmpty()){
                errors.addAll(passwordErrors);

            }
            if (!usernameErrors.isEmpty()){
                errors.addAll(usernameErrors);
            }
            if (!emailError.equals("")){
                errors.add(emailError);
            }
            JSONArray errorsJson = new JSONArray();
            for (String error : errors) {
                errorsJson.add(error);
            }
            message.put("errors", errorsJson);

        }

        System.out.println(message.toString());
        return message;

    }

    public Boolean validateEmail(String email) {
        Boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            result = false;
        }
        return result;

    }

    public ArrayList<String> validateUsername(String username) {
        ArrayList<String> usernameErrors = new ArrayList<>();


        String regex = "^[a-zA-Z0-9_.-]*$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(username);

        if (!matcher.matches()) {
            usernameErrors.add("Username can only contain lower- and uppercase letters, numbers and .-_");

        }

        String regex2 = "^.{5,25}$";

        pattern = Pattern.compile(regex2);

        matcher = pattern.matcher(username);


        if (!matcher.matches()) {
            usernameErrors.add("Username must be between 5 and 25 characters long");
        }


        return usernameErrors;


    }

    public ArrayList<String> validatePassword(String password) {
        ArrayList<String> passwErrors = new ArrayList<>();

        String regex = "^.{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()){
            passwErrors.add("Password must contain at least 8 characters");
        }

        String regex2 = "^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!#$%&?\"]).*$";

        pattern = Pattern.compile(regex2);
        matcher = pattern.matcher(password);

        if(!matcher.matches()){
            passwErrors.add("Password can only contain alphabetical values, numbers and special signs [!#$%&?\\\"] and must contain at least one of each of these");
        }
        return passwErrors;
    }

}
