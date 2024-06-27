package com.stackmobile.fit_journey;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.stackmobile.fit_journey.database.entities.User;
import com.stackmobile.fit_journey.databinding.RegisterFragmentBinding;
import com.stackmobile.fit_journey.database.LocalDatabase;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterUserFragment extends Fragment {
    private RegisterFragmentBinding binding;
    private LocalDatabase localDatabase;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = RegisterFragmentBinding.inflate(inflater, container, false);

        localDatabase = LocalDatabase.getDatabase(requireContext());

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::registrarUsuarioNoDB);

        binding.buttonLogin.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_to_LoginFragment)
        );
    }

    private void registrarUsuarioNoDB(View view) {
        String name = binding.editTextName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "O campo 'Nome' é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = binding.editTextEmail.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "O campo 'Email' é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = binding.editTextPassword.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "O campo 'Senha' é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Digite um E-mail que seja válido", Toast.LENGTH_SHORT).show();
            return;
        }

        User usuarioParaTeste = localDatabase.userModel().findByEmail(email);
        if (usuarioParaTeste != null) {
            Toast.makeText(requireContext(), "Este E-mail já está cadastrado em outro usuário", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        User usuario = new User();
        usuario.setName(name);
        usuario.setEmail(email);
        usuario.setPassword(passwordHash);

        localDatabase.userModel().insert(usuario);
        Toast.makeText(requireContext(), "Usuário cadastrado com sucesso",
                Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_to_LoginFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
