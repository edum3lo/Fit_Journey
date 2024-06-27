package com.stackmobile.fit_journey;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.stackmobile.fit_journey.database.LocalDatabase;
import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.FragmentLoginBinding;

import org.mindrot.jbcrypt.BCrypt;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LocalDatabase localDatabase;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        localDatabase = LocalDatabase.getDatabase(requireContext());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(this::login);

        binding.buttonRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_Register_User)
        );
    }

    private void login(View view) {
        String email = binding.editTextEmail.getText().toString();
        if(email.equals("")) {
            Toast.makeText(requireContext(), "O email é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = binding.editTextPassword.getText().toString();
        if(password.equals("")) {
            Toast.makeText(requireContext(), "A senha é obrigatória", Toast.LENGTH_SHORT).show();
            return;
        }

        User usuarioParaTeste = localDatabase.userModel().findByEmail(email);
        if (usuarioParaTeste != null) {
            String hash = usuarioParaTeste.getPassword();
            if (!BCrypt.checkpw(password, hash)) {
                Toast.makeText(requireContext(), "O campo 'Senha' está incorreto", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("usuario", usuarioParaTeste);

                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_home, bundle);
            }
        } else {
            Toast.makeText(requireContext(), "O campo 'E-mail' está incorreto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
