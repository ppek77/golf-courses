package info.pekny.golfcourses.user;

import jakarta.validation.constraints.NotBlank;

public class ProfileEditForm {

    @NotBlank
    private String fullName;

    private String newPassword;

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
