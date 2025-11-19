package com.aragon.apiapplication.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.aragon.apiapplication.models.LoginResponse;
import com.aragon.apiapplication.models.LogoutResponse;
import com.aragon.apiapplication.models.RegisterResponse;
import com.aragon.apiapplication.network.RetrofitClient;
import com.aragon.apiapplication.repository.ApiRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends ViewModel {

    private ApiRepository repository;
    private MutableLiveData<LoginResponse> loginResult;
    private MutableLiveData<RegisterResponse> registerResult;
    private MutableLiveData<Boolean> logoutResult;
    private MutableLiveData<String> errorMessage;

    public AuthViewModel() {
        repository = new ApiRepository();
        loginResult = new MutableLiveData<>();
        registerResult = new MutableLiveData<>();
        logoutResult = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public LiveData<RegisterResponse> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Boolean> getLogoutResult() {
        return logoutResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        repository.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse login = response.body();
                    RetrofitClient.setToken(login.getToken());
                    loginResult.setValue(login);
                } else {
                    errorMessage.setValue("Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void register(String name, String lastName, String email,
                         String password, String confirmPassword) {
        repository.register(name, lastName, email, password, confirmPassword)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            registerResult.setValue(response.body());
                        } else {
                            errorMessage.setValue("Error al registrar usuario");
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        errorMessage.setValue("Error de conexión: " + t.getMessage());
                    }
                });
    }

    public void logout() {
        repository.logout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    RetrofitClient.clearToken();
                    logoutResult.setValue(true);
                } else {
                    errorMessage.setValue("Error al cerrar sesión");
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
