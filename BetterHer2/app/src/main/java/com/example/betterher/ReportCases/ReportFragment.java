package com.example.betterher.ReportCases;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.betterher.MainActivity;
import com.example.betterher.R;
import com.example.betterher.TrackCases.TrackCasesChildItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ReportFragment extends Fragment {

    MainActivity mainActivity;
    FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseStorage mStorage;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private String strRole, strIncidentType, strDate, strTime, strLocation, strDescription;
    private ArrayList<SpinnerItem> roleItems;
    private RoleAdapter roleAdapter;
    private Spinner roleSpinner;
    private ArrayList<SpinnerItem> incidentTypeItems;
    private IncidentTypeAdapter incidentTypeAdapter;
    private Spinner incidentTypeSpinner;
    private AppCompatButton btnDatePicker, btnTimePicker;
    private Place selectedPlace;
    private ConstraintLayout clPlacePicker;
    private static final int PLACE_PICKER_REQUEST = 2;
    private TextView tvLocationError, tvSelectLocation;
    private EditText etDescription;
    private RelativeLayout btnImagePicker;
    private ViewPager vpImages;
    private Uri imageUri;
    private ArrayList<Uri> imagesList;
    private ArrayList<String> urlsList;
    private Button btnSubmitReport;
    private ProgressBar progressBar;
    private ImageView ivCaution;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        mainActivity = (MainActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        //initialise list for role and incidentType spinner
        initList();

        //role
        roleSpinner = view.findViewById(R.id.spinner_role);
        roleAdapter = new RoleAdapter(requireContext(), roleItems);
        roleSpinner.setAdapter(roleAdapter);
        roleSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(i);
                        strRole = clickedItem.getItemName().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        //incident type
        incidentTypeSpinner = view.findViewById(R.id.spinner_incident_type);
        incidentTypeAdapter = new IncidentTypeAdapter(requireContext(), incidentTypeItems);
        incidentTypeSpinner.setAdapter(incidentTypeAdapter);
        incidentTypeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        SpinnerItem clickedItem = (SpinnerItem) adapterView.getItemAtPosition(i);
                        strIncidentType = clickedItem.getItemName().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        //date picker button
        btnDatePicker = view.findViewById(R.id.btn_date_picker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //time picker button
        btnTimePicker = view.findViewById(R.id.btn_time_picker);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10) {
                                    btnTimePicker.setText(hourOfDay + ":0" + minute);
                                    strTime = hourOfDay + ":0" + minute;
                                } else {
                                    btnTimePicker.setText(hourOfDay + ":" + minute);
                                    strTime = hourOfDay + ":" + minute;
                                }
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        //location picker button
        clPlacePicker = view.findViewById(R.id.cl_place_picker);
        tvSelectLocation = view.findViewById(R.id.tv_select_location);
        tvLocationError = view.findViewById(R.id.tv_location_error);
        clPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlacePicker();
            }
        });

        //description
        etDescription = view.findViewById(R.id.et_incident_desc);

        //Evidence
        btnImagePicker = view.findViewById(R.id.rl_image_picker);
        vpImages = view.findViewById(R.id.vp_images);
        imagesList = new ArrayList<>();
        urlsList = new ArrayList<>();
        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermission();
                PickImageFromGallery();
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        ivCaution = view.findViewById(R.id.iv_caution);

        //submit report button
        btnSubmitReport = view.findViewById(R.id.btn_submit_report);
        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strDescription = etDescription.getText().toString();
                if (checkAllFields()) {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage(strRole, strIncidentType, strDate, strTime, strLocation, strDescription);
                } else {
                    ivCaution.setVisibility(View.VISIBLE);
                    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(ivCaution, "alpha", 1f, 0f);
                    fadeOut.setDuration(3000);
                    fadeOut.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ivCaution.setVisibility(View.GONE);
                        }
                    });
                    fadeOut.start();
                }
            }
        });

        return view;
    }

    private void openPlacePicker() {
        if (!Places.isInitialized()) {
            String apiKey = "AIzaSyBZh1HqlRpwI_tqZ51r-qpf-HKYyWgeh54";
            Places.initialize(requireContext(), apiKey);
        }
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, Arrays.asList(Place.Field.ID, Place.Field.NAME))
                .build(requireContext());
        startActivityForResult(intent, PLACE_PICKER_REQUEST);
    }

    private boolean checkAllFields() {
        if (strRole.equals("Select Role") || strRole == null) {
            roleSpinner.requestFocus();
            TextView errorText = (TextView) ((ViewGroup) roleSpinner.getSelectedView()).getChildAt(0);
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.red));
            errorText.setText("Role in incident is required  ");
            return false;
        }
        if (strIncidentType.equals("Select Type Of Incident") || strIncidentType == null) {
            incidentTypeSpinner.requestFocus();
            TextView errorText = (TextView) ((ViewGroup) incidentTypeSpinner.getSelectedView()).getChildAt(0);
            errorText.setError("");
            errorText.setTextColor(getResources().getColor(R.color.red));
            errorText.setText("Type of incident is required  ");
            return false;
        }
        if (strDate == null) {
            btnDatePicker.setError("Date of incident is required");
            return false;
        }
        if (strTime == null) {
            btnTimePicker.setError("Time of incident is required");
            return false;
        }
        if (strLocation == null) {
            tvLocationError.setVisibility(View.VISIBLE);
            tvSelectLocation.setVisibility(View.INVISIBLE);
            tvLocationError.setText("Location of incident is required");
            tvLocationError.setFocusable(false);
            tvLocationError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvLocationError.setVisibility(View.GONE);
                    tvSelectLocation.setVisibility(View.VISIBLE);
                }
            });
            return false;
        }
        if (strDescription.trim().isEmpty()) {
            etDescription.setError("Description is required");
            return false;
        }
        return true;
    }

    private void uploadImage(String strRole, String strIncidentType, String strDate, String strTime, String strLocation, String strDescription) {
        if (imagesList.size() != 0) {
            for (int i = 0; i < imagesList.size(); i++) {
                Uri individualImage = imagesList.get(i);
                if (individualImage != null) {
                    StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("ItemImages");
                    final StorageReference imageName = imageFolder.child("Image" + i + ": " + individualImage.getLastPathSegment());
                    imageName.putFile(individualImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            urlsList.add(String.valueOf(uri));
                                            if (urlsList.size() == imagesList.size()) {
                                                saveData(strRole, strIncidentType, strDate, strTime, strLocation, strDescription, urlsList);
                                            }
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        } else {
            saveData(strRole, strIncidentType, strDate, strTime, strLocation, strDescription, urlsList);
        }
    }

    private void saveData(String strRole, String strIncidentType, String strDate, String strTime, String strLocation, String strDescription, ArrayList<String> urlsList) {
        if (!TextUtils.isEmpty(strRole) &&
                !TextUtils.isEmpty(strIncidentType) &&
                !TextUtils.isEmpty(strDate) &&
                !TextUtils.isEmpty(strTime) &&
                !TextUtils.isEmpty(strLocation) &&
                !TextUtils.isEmpty(strDescription)) {
            Case caseItem = new Case(strRole, strIncidentType, strDate, strTime, strLocation, strDescription, urlsList, "", "");

            //TODO: test userUid
            //firebaseAuth = FirebaseAuth.getInstance();
            //user = firebaseAuth.getCurrentUser();
            String userUid = "bEBpClWNtU4U0BnnB8jX";

            db.collection("Users").document(userUid).collection("Cases").add(caseItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    caseItem.setCaseId(documentReference.getId());
                    caseItem.setCaseStatus("In Progress");

                    //pass caseID to ReportTQFragment
                    Bundle bundle = new Bundle();
                    bundle.putString("caseID", caseItem.getCaseId());
                    ReportTQFragment reportTQFragment = new ReportTQFragment();
                    reportTQFragment.setArguments(bundle);
                    progressBar.setVisibility(View.VISIBLE);
                    mainActivity.replaceFragment(reportTQFragment);

                    db.collection("Users").document(userUid).collection("Cases").document(caseItem.getCaseId())
                            .set(caseItem, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Add progress information
                                    Date currentDateTime = new Date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
                                    String progressDate = dateFormat.format(currentDateTime);
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                    String progressTime = timeFormat.format(currentDateTime);
                                    String progressTitle = "Submission Received";
                                    String progressDesc = "The submitted case is now in the process of being meticulously reviewed by our dedicated team.";
                                    String caseStatus = "In Progress";
                                    TrackCasesChildItem progress = new TrackCasesChildItem(progressDate, progressTime, progressTitle, progressDesc, caseStatus);

                                    db.collection("Users").document(userUid).collection("Cases").document(caseItem.getCaseId())
                                            .collection("Progress").add(progress)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                }
                            });
                }
            });
        }
    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                PickImageFromGallery();
            }
        } else {
            PickImageFromGallery();
        }
    }

    private void PickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                imageUri = data.getClipData().getItemAt(i).getUri();
                imagesList.add(imageUri);
                ImagesViewPagerAdapter imagesViewPagerAdapter = new ImagesViewPagerAdapter(requireContext(), imagesList);
                vpImages.setAdapter(imagesViewPagerAdapter);
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                strLocation = place.getName().toString();
                tvSelectLocation.setText(strLocation);
            }
        }
    }

    private void initList() {
        //role
        roleItems = new ArrayList<>();
        String[] roles = getResources().getStringArray(R.array.dropdown_roles);
        roleItems.add(new SpinnerItem("Select Role"));
        for (String role : roles) {
            roleItems.add(new SpinnerItem(role));
        }

        //type of incident
        incidentTypeItems = new ArrayList<>();
        String[] incidentTypes = getResources().getStringArray(R.array.dropdown_incident_types);
        incidentTypeItems.add(new SpinnerItem("Select Type Of Incident"));
        for (String incidentType : incidentTypes) {
            incidentTypeItems.add(new SpinnerItem(incidentType));
        }
    }
}