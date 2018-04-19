package technource.autoreum.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.activities.AskTheCrowd.AskTheCrowdStepOne;
import technource.autoreum.activities.EditRegisteredCarActivity;
import technource.autoreum.activities.PostJob.PostNewJobStepOne;
import technource.autoreum.activities.RegisterdCarsScreen;
import technource.autoreum.model.RegisteredCarDBO;
import technource.autoreum.activities.ViewHistoryGarageActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;

import static android.app.Activity.RESULT_OK;

/**
 * Created by technource on 25/9/17.
 */

public class RecycleradapterforRegisteredcar extends
    RecyclerView.Adapter<RecycleradapterforRegisteredcar.Recycleviewholder> {

  public static String Car_id;
  public static int position_ = 0;
  Context context;
  int PERMISSION_CODE = 2;
  int CAMERA_CODE = 0;
  int GALLARY_CODE = 1;
  Bitmap bitmap;
  Recycleviewholder holder;
  ArrayList<RegisteredCarDBO> registeredCarDBOs;
  LoginDetail_DBO loginDetail_dbo;
  JSONObject jsonObject;
  RegisteredCarDBO registeredCarDBO;
  private File file_1;

  public RecycleradapterforRegisteredcar(ArrayList<RegisteredCarDBO> services, Context context) {
    this.context = context;
    this.registeredCarDBOs = services;
    loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
    registeredCarDBO = new RegisteredCarDBO();
  }


  @Override
  public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_row_registeredcar, parent, false);
    Recycleviewholder recycleviewholder = new Recycleviewholder(view);

    return recycleviewholder;
  }

  @Override
  public void onBindViewHolder(final Recycleviewholder holder, final int position) {

    this.holder = holder;
    this.holder.carname.setText(
        registeredCarDBOs.get(position).getCarmake() + " " + registeredCarDBOs.get(position)
            .getCarmodel());

    if (registeredCarDBOs.get(position).getCar_img() != null) {
      Glide.with(context)
          .load(
              WebServiceURLs.BASE_URL_IMAGE_PROFILE + registeredCarDBOs.get(position).getCar_img())
          .asBitmap()
          .dontAnimate()
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .placeholder(R.drawable.no_user)
          .error(R.drawable.no_user)
          .skipMemoryCache(true)
          .into(this.holder.iv_image);

    }
    this.holder.viewhistory.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context.getApplicationContext(),
            ViewHistoryGarageActivity.class);
        intent.putExtra("registeredcar", registeredCarDBOs.get(position));
        context.startActivity(intent);
        ((RegisterdCarsScreen) context).activityTransition();
      }
    });

    this.holder.edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context.getApplicationContext(),
            EditRegisteredCarActivity.class);
        intent.putExtra("registeredcar", registeredCarDBOs.get(position));
        context.startActivity(intent);
        ((RegisterdCarsScreen) context).activityTransition();
      }
    });
    this.holder.postjob.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context.getApplicationContext(), PostNewJobStepOne.class);
        intent.putExtra("registeredcar", registeredCarDBOs.get(position));
        context.startActivity(intent);
        ((RegisterdCarsScreen) context).activityTransition();
      }
    });
    this.holder.askthecrowd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context.getApplicationContext(), AskTheCrowdStepOne.class);
        intent.putExtra("registeredcar", registeredCarDBOs.get(position));
        context.startActivity(intent);
        ((RegisterdCarsScreen) context).activityTransition();
      }
    });
    this.holder.iv_image.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        position_ = position;
        registeredCarDBO = registeredCarDBOs.get(position);
        Car_id = registeredCarDBOs.get(position).getId();
        AppLog.Log("size of object", "size-->" + registeredCarDBO.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          RuntimeCameraGalleryPermission();
        } else {
          selectImage();
        }
      }
    });
  }

  private void RuntimeCameraGalleryPermission() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
          != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
          Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions((Activity) context,
            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
            PERMISSION_CODE);
      } else {
        selectImage();
      }
    } else {

      selectImage();
    }
  }

  private void selectImage() {

    final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Add Photo!");
    builder.setItems(options, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int item) {
        if (options[item].equals("Take Photo")) {
          File f = new File(Environment.getExternalStorageDirectory(), "temp1.jpg");
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          try {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ((RegisterdCarsScreen) context).startActivityForResult(intent1, CAMERA_CODE);

          } catch (ActivityNotFoundException e) {
          }
        } else if (options[item].equals("Choose from Gallery")) {
          Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
          photoPickerIntent.setType("image/*");
          ((RegisterdCarsScreen) context).startActivityForResult(photoPickerIntent, GALLARY_CODE);
        } else if (options[item].equals("Cancel")) {
          dialog.dismiss();
        }
      }
    });
    builder.show();
    builder.setCancelable(false);
  }


  @Override
  public int getItemCount() {
    return registeredCarDBOs.size();
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    AppLog.Log("Tag", "you are in activityresul");
    if (requestCode == GALLARY_CODE) {
      if (resultCode == RESULT_OK) {
        onSelectFromGalleryResult(data);
      }
    } else if (requestCode == CAMERA_CODE) {
      if (resultCode == RESULT_OK) {
        onCaptureImageResult(data);
      }
    }

  }

  @SuppressWarnings("deprecation")
  private void onSelectFromGalleryResult(Intent data) {
    bitmap = null;
    if (data != null) {
      try {
        bitmap = MediaStore.Images.Media
            .getBitmap(context.getApplicationContext().getContentResolver(), data.getData());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        Uri fileUri = getImageUri(context, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));

        saveProfileImage(data);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void onCaptureImageResult(Intent data) {

    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
    File destination = new File(Environment.getExternalStorageDirectory(),
        System.currentTimeMillis() + ".jpg");
    FileOutputStream fo;
    try {
      destination.createNewFile();
      fo = new FileOutputStream(destination);
      fo.write(bytes.toByteArray());
      fo.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    Uri fileUri = getImageUri(context, thumbnail);
    file_1 = new File(getRealPathFromURI(fileUri));

    saveProfileImage(data);
  }

  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
        context.getResources().getString(R.string.app_name) + "_" + System.currentTimeMillis(),
        null);
    return Uri.parse(path);
  }

  public String getRealPathFromURI(Uri uri) {
    Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
    cursor.moveToFirst();
    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    return cursor.getString(idx);
  }

  private void saveProfileImage(final Intent data) {
    new AsyncTask<Void, Void, Void>() {
      String status;
      String result = "";
      JSONObject response;


      @Override
      protected void onPreExecute() {
        super.onPreExecute();

        ((RegisterdCarsScreen) context).showLoadingDialog(true);
      }

      @Override
      protected Void doInBackground(Void... params) {

        ResponseHandler<String> res = new BasicResponseHandler();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        try {

          if (file_1 != null && file_1.length() > 1) {
            try {
              AppLog.Log("TAG", "Image path " + file_1);
              FileBody t_fileBody = new FileBody(file_1);
              reqEntity.addPart("car_img", t_fileBody);
            } catch (Exception e) {
              e.printStackTrace();
              reqEntity.addTextBody("car_img", "");
            }
          } else {
            reqEntity.addTextBody("car_img", "");
          }

          reqEntity.addTextBody(Constants.SIGN_UP.SERVICE_NAME, "edit_car_img");
          reqEntity.addTextBody(Constants.SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
          reqEntity.addTextBody("car_id", "" + Car_id);

          AppLog.Log("car_id", "-->" + Car_id);
        } catch (Exception e) {
          e.printStackTrace();
        }

        HttpEntity httpEntity = reqEntity.build();
        httpPost.setEntity(httpEntity);

        try {
          result = httpClient.execute(httpPost, res);
          if (result != null) {
            AppLog.Log("Tag", "Response in Update Profile : " + result.toString());
            response = new JSONObject(result);

            AppLog.Log("Tag", "Response in Update Profile : " + result.toString());
            try {
              status = response.getString(Constants.STATUS);
              AppLog.Log("Tag", "Status: " + status);
              jsonObject = response.getJSONObject("data");

            } catch (JSONException e) {
              AppLog.Log("Tag", "Erro in onResponse : " + e.getMessage());
              e.printStackTrace();
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          AppLog.Log("Tag", "Error in Update Profile" + e.getMessage());
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ((RegisterdCarsScreen) context).showLoadingDialog(false);
        try {
          if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
            Toast.makeText(context, R.string.profile_updated, Toast.LENGTH_SHORT).show();

            RegisteredCarDBO model = new RegisteredCarDBO();
            model.setCar_img(jsonObject.getString("image"));
            model.setId(registeredCarDBOs.get(position_).getId());
            model.setRegistration_number(registeredCarDBOs.get(position_).getRegistration_number());
            model.setCarmake(registeredCarDBOs.get(position_).getCarmake());
            model.setCarmodel(registeredCarDBOs.get(position_).getCarmodel());
            registeredCarDBOs.set(position_, model);
            ((RegisterdCarsScreen) context).adapter.notifyDataSetChanged();

          } else if (status != null && status.equalsIgnoreCase(Constants.FAILURE)) {
            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
          }
        } catch (JSONException e) {
          AppLog.Log("tag", "Erro in onResponse : " + e.getMessage());
          e.printStackTrace();
        }
      }
    }.execute();

  }

  public static class Recycleviewholder extends RecyclerView.ViewHolder {

    TextView carname, registration_number, viewhistory, edit, postjob, askthecrowd;
    ImageView iv_image;
    private View view;

    public Recycleviewholder(View itemView) {
      super(itemView);
      view = itemView;
      carname = itemView.findViewById(R.id.carname);
      registration_number = itemView.findViewById(R.id.registration_number);
      viewhistory = itemView.findViewById(R.id.viewhistory);
      edit = itemView.findViewById(R.id.edit);
      postjob = itemView.findViewById(R.id.postjob);
      askthecrowd = itemView.findViewById(R.id.askthecrowd);
      iv_image = itemView.findViewById(R.id.iv_image);
    }

  }
}
