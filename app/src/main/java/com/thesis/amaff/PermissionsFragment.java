package com.thesis.amaff;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

/** Handles requesting permissions for the device before using the camera */
public class PermissionsFragment extends Fragment {

    /**
     * Convenience method used to check if all permissions required by this
     * app are granted
     */

    Button captureBtn;
    public static boolean hasPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher
            = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(),
                                    "Permission request granted",
                                    Toast.LENGTH_LONG)
                            .show();
                    navigateToCamera();
                } else {
                    Toast.makeText(requireContext(),
                                    "Permission request denied",
                                    Toast.LENGTH_LONG)
                            .show();
                }
            });

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            navigateToCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void navigateToCamera() {
//        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                .navigate(PermissionsFragmentDirections.actionPermissionsToCamera());
        Fragment fragment = new CameraFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .commit();
    }


//        captureBtn = requireActivity().findViewById(R.id.captureBtn);
//        captureBtn.setOnClickListener(v -> {
//            navigateToCamera();
//        });
//    }




}
