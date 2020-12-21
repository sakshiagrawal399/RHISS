package rural.rhiss.Upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.droidbyme.dialoglib.AnimUtils;
import com.droidbyme.dialoglib.DroidDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import rural.rhiss.R;
import rural.rhiss.RHISS;
import rural.rhiss.Util.Constants;
import rural.rhiss.Util.DateFormat;
import rural.rhiss.Util.GlobalLocationService;
import rural.rhiss.Util.ImageCompress;
import rural.rhiss.Util.TextViewCustom;
import rural.rhiss.Util.Utils;
import rural.rhiss.Util.WebServiceCall;

public class UploadPhotoActivity extends AppCompatActivity implements Constants {

    double current_latitude = 0, current_longitude = 0, current_accuracy = 0;
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    private static final int CAMERA_REQUEST1 = 1888, CAMERA_REQUEST2 = 1889, CAMERA_REQUEST3 = 1890;
    Uri imageUri = null;
    String image1 = "", image2 = "", image3 = "";

    private TextViewCustom latitudeTextView, longitudeTextView, accuracyTextView, addressTextView, dateTextView, inspectionLevelTextView, headTextView;
    private ImageView imageView1, imageView2, imageView3;
    private Button uploadButton, cancelButton;
    private EditText remarkEditText;
    private KProgressHUD kProgressHUD;
    HashMap<String, String> hashMap;

    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        latitudeTextView = findViewById(R.id.textViewLatitude);
        longitudeTextView = findViewById(R.id.textViewLongitude);
        accuracyTextView = findViewById(R.id.textViewAccuracy);
        addressTextView = findViewById(R.id.textViewAddress);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        uploadButton = findViewById(R.id.buttonUpload);
        remarkEditText = findViewById(R.id.editTextRemark);
        dateTextView = findViewById(R.id.textViewInspDate);
        inspectionLevelTextView = findViewById(R.id.textViewInspLevel);
        headTextView = findViewById(R.id.loginText);
        cancelButton = findViewById(R.id.buttonCancel);

        hashMap = RHISS.getBeneficiaryDetailObject().getHashMap();
        headTextView.setText(hashMap.get("Name") + "/ " + hashMap.get("Reg_code"));
        inspectionLevelTextView.setText(": " + hashMap.get("HouseStatusCode"));

        dateTextView.setText(": " + DateFormat.currentDate());
        startTimer();

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST1);

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST2);

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST3);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void upload() {
        kProgressHUD = KProgressHUD.create(UploadPhotoActivity.this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel(getString(R.string.loading_data))
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        if (current_longitude == 0) {
            kProgressHUD.dismiss();
            new DroidDialog.Builder(UploadPhotoActivity.this)
                    .icon(R.drawable.info_icon)
                    .title(getString(R.string.error))
                    .content(getString(R.string.invalid_location_details))
                    .cancelable(true, true)
                    .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                        @Override
                        public void onPositive(Dialog droidDialog) {
                            droidDialog.dismiss();
                        }
                    })
                    .color(ContextCompat.getColor(UploadPhotoActivity.this, R.color.app_color), ContextCompat.getColor(UploadPhotoActivity.this, R.color.white),
                            ContextCompat.getColor(UploadPhotoActivity.this, R.color.colorPrimary))
                    .animation(AnimUtils.AnimFadeInOut)
                    .show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("RegCode", hashMap.get("Reg_code"));
                jsonObject.put("HouseStatus", hashMap.get("HouseStatusCode"));
                jsonObject.put("EntryDate", DateFormat.currentDate());
                jsonObject.put("UploadedBy", hashMap.get("PLI_Code"));
                jsonObject.put("StateCode", hashMap.get("LGD_State_Code"));
                jsonObject.put("DistrictCode", hashMap.get("LGD_District_Code"));
                jsonObject.put("BlockCode", hashMap.get("LGD_Block_Code"));
                jsonObject.put("GPCode", hashMap.get("LGD_GP_Code"));
                jsonObject.put("VillageCode", hashMap.get("LGD_Village_code"));
                jsonObject.put("inspectionMode", "0");
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < 3; i++) {
                    JSONObject imageJsonObject = new JSONObject();
                    imageJsonObject.put("InspectionId", hashMap.get("Reg_code") + "-" + hashMap.get("HouseStatusCode") + "-" + (i + 1));
                    imageJsonObject.put("Latitude", String.valueOf(current_latitude));
                    imageJsonObject.put("Longitude", String.valueOf(current_longitude));
                    imageJsonObject.put("Accuracy", String.valueOf(current_accuracy));
                    imageJsonObject.put("Comment", remarkEditText.getText());
                    imageJsonObject.put("Deviation", "1.2");
                    imageJsonObject.put("Location", addressTextView.getText());
                    if (i == 0) {
                        imageJsonObject.put("imageData", image1);
                    } else if (i == 1) {
                        imageJsonObject.put("imageData", image2);
                    } else {
                        imageJsonObject.put("imageData", image3);
                    }
                    jsonArray.put(imageJsonObject);
                }
                jsonObject.put("images", jsonArray);

            } catch (Exception e) {
                e.printStackTrace();
            }


            WebServiceCall.getWebServiceCallInstance(APP_URL + UPLOAD_PHOTO_URL).post(jsonObject, getApplicationContext()).executeAsync(new WebServiceCall.WebServiceCallBackHandler() {
                @Override
                public void onServiceCallSucceed(String serviceName, String response) {
                    kProgressHUD.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        new DroidDialog.Builder(UploadPhotoActivity.this)
                                .icon(R.drawable.info_icon)
                                .title(getString(R.string.success))
                                .content(jsonObject.getString("message"))
                                .cancelable(false, false)
                                .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                    @Override
                                    public void onPositive(Dialog droidDialog) {
                                        droidDialog.dismiss();
                                        finish();
                                    }
                                })
                                .color(ContextCompat.getColor(UploadPhotoActivity.this, R.color.app_color), ContextCompat.getColor(UploadPhotoActivity.this, R.color.white),
                                        ContextCompat.getColor(UploadPhotoActivity.this, R.color.colorPrimary))
                                .animation(AnimUtils.AnimFadeInOut)
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onServiceStatusFailed(String serviceName, String response) {
                    kProgressHUD.dismiss();
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        message = jsonObject.getString("message");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new DroidDialog.Builder(UploadPhotoActivity.this)
                            .icon(R.drawable.info_icon)
                            .title(getString(R.string.error))
                            .content(message)
                            .cancelable(true, true)
                            .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                @Override
                                public void onPositive(Dialog droidDialog) {
                                    droidDialog.dismiss();
                                }
                            })
                            .color(ContextCompat.getColor(UploadPhotoActivity.this, R.color.app_color), ContextCompat.getColor(UploadPhotoActivity.this, R.color.white),
                                    ContextCompat.getColor(UploadPhotoActivity.this, R.color.colorPrimary))
                            .animation(AnimUtils.AnimFadeInOut)
                            .show();

                }

                @Override
                public void onServiceCallFailed(String serviceName, Exception e) {
                    kProgressHUD.dismiss();
                    new DroidDialog.Builder(UploadPhotoActivity.this)
                            .icon(R.drawable.info_icon)
                            .title(getString(R.string.error))
                            .content(getString(R.string.unable_process))
                            .cancelable(true, true)
                            .positiveButton(getString(R.string.ok), new DroidDialog.onPositiveListener() {
                                @Override
                                public void onPositive(Dialog droidDialog) {
                                    droidDialog.dismiss();
                                }
                            })
                            .color(ContextCompat.getColor(UploadPhotoActivity.this, R.color.app_color), ContextCompat.getColor(UploadPhotoActivity.this, R.color.white),
                                    ContextCompat.getColor(UploadPhotoActivity.this, R.color.colorPrimary))
                            .animation(AnimUtils.AnimFadeInOut)
                            .show();

                }
            }, "Upload Photo");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView1.setImageBitmap(imageBitmap);
            image1 = ImageCompress.encode(imageBitmap);
            imageView2.setVisibility(View.VISIBLE);
        }
        if (requestCode == CAMERA_REQUEST2 && resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(imageBitmap);
            image2 = ImageCompress.encode(imageBitmap);

            imageView3.setVisibility(View.VISIBLE);
        }
        if (requestCode == CAMERA_REQUEST3 && resultCode == Activity.RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView3.setImageBitmap(imageBitmap);
            image3 = ImageCompress.encode(imageBitmap);

        }
    }

    private void startTimer() {
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        updateLocation();
                        latitudeTextView.setText(": " + String.valueOf(Math.round(current_latitude * 1000000.0) / 1000000.0));
                        longitudeTextView.setText(": " + String.valueOf(Math.round(current_longitude * 1000000.0) / 1000000.0));
                        accuracyTextView.setText(": " + String.valueOf(Math.round(current_accuracy * 100.0) / 100.0) + " mtrs");
                        if (Utils.isNetworkAvailable(getApplicationContext())) {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(current_latitude, current_longitude, 1);
                                if (addresses.get(0).getAddressLine(0) != null) {
                                    addressTextView.setText(addresses.get(0).getAddressLine(0));
                                } else {
                                    addressTextView.setText("");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            }
        };
        mTimer1.schedule(mTt1, 1, 5000);

    }

    public void updateLocation() {
        Intent serviceIntent = new Intent(UploadPhotoActivity.this, GlobalLocationService.class);
        startService(serviceIntent);
        if (Utils.latitude != null) {
            current_latitude = Utils.latitude;
            current_longitude = Utils.longitude;
            current_accuracy = Utils.accuracy;
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    private void stopTimer() {
        if (mTimer1 != null) {
            mTimer1.cancel();
            mTimer1.purge();
        }
    }
}
