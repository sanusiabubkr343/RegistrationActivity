package com.example.registrationactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final int GALLERY_CODE = 1;
    @BindView(R.id.linearLayout)
    ConstraintLayout linearLayout;
    private Uri imageUri;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.uploadImageButton)
    Button uploadImageButton;
    @BindView(R.id.nameText)
    EditText nameText;
    @BindView(R.id.phoneText)
    EditText phoneText;
    @BindView(R.id.usernameText)
    EditText usernameText;
    @BindView(R.id.addressText)
    EditText addressText;
    @BindView(R.id.disciplineText)
    EditText disciplineText;
    @BindView(R.id.saveButton)
    Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.uploadImageButton, R.id.saveButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uploadImageButton:
                uploadImage();
                break;
            case R.id.saveButton:
                saveData();
                break;
        }
    }

    private void saveData() {
        String name = nameText.getText().toString().trim();
        String username = usernameText.getText().toString().trim();
        String address = addressText.getText().toString().trim();
        String discipline = disciplineText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        if (!name.isEmpty() && !username.isEmpty() && !address.isEmpty() &&! discipline.isEmpty() && !phone.isEmpty()&& !(imageUri==null)){
            uploadDataToServer(name,username,address,discipline,phone,imageUri);

        } else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout,"Empty Field not Accepted", BaseTransientBottomBar.LENGTH_LONG);
            snackbar.show();
        }

    }

    private void uploadDataToServer(String name, String username,String  address,String discipline ,String phone,Uri uri) {
        Model model= new Model();
        APIInterface apiInterface  =APICLIENT.getClient().create(APIInterface.class);
        model.setUsername(username);
        model.setFullName(name);
        model.setAddress(address);
        model.setDiscipline(discipline);
        model.setPhoneNumber(phone);
        model.setImageUrl(uri.toString());
        //model.setImageUrl("www.I am coming.com/jpeg");
        File file = new File(imageUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Call<ServerResponse> call = apiInterface.upload(fileToUpload,filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                assert serverResponse != null;
                Log.d("check", "onResponse: "+serverResponse.toString());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });


  Call<Model> callResources = apiInterface.getResources(model);
        callResources.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                assert response.body() != null;
                Log.d("sentPost", String.format("onResponse: %s", response.body().getFullName()));
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d("inner", "onFailure: "+t.getMessage());
            }
        });


    }

    private void uploadImage() {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == GALLERY_CODE && resultCode == RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).fit().into(imageView);
        }
    }


}
