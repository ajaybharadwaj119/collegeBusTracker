package com.akinfopark.colgbustracking.Utils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class AppFunctions {
    Context context;


    /*  public static void setMapStyle(GoogleMap googleMap) {
          try {
              // Customise the styling of the base map using a JSON object defined
              // in a raw resource file.
              boolean success = googleMap.setMapStyle(
                      MapStyleOptions.loadRawResourceStyle(
                              App.getInstance(), R.raw.map_night));

              if (!success) {
                  Log.e("MapStyle", "Style parsing failed.");
              }
          } catch (Resources.NotFoundException e) {
              Log.e("MapStyle", "Can't find style. Error: ", e);
          }
      }
  */
    public static void setAlertDialogBg(Dialog dialog, Drawable drawable) {
        dialog.getWindow().setBackgroundDrawable(drawable);
    }

    public static void hideAndShowViews(List<View> views, View view) {
        for (View v : views
        ) {
            v.setVisibility(GONE);
        }
        view.setVisibility(VISIBLE);
    }

    public static String getCurrentTimeChat() {
        return DataFormats.dateFormatTimeStampChat.format(new Date());
    }

    public static long getCurrentTimeStampChat() {
        return System.currentTimeMillis() / 1000L;
    }

    public static boolean validImageFormatChat(String filePath, String fileType) {
        final String extension = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        if (fileType.equalsIgnoreCase("image"))
            return (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png"));
        return true;
    }

    /*public static void hideAndShowViews(List<ExpandableLayout> views, ExpandableLayout view) {
        for (ExpandableLayout v : views
        ) {
            v.setExpanded(false);
        }
        view.setExpanded(true);
    }*/

    public static JSONObject get0object(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("result");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        return jsonObject;
    }

    public static String getAPIMsg(JSONObject response) throws JSONException {
        JSONObject jsonObject = response.getJSONObject("message");
        return jsonObject.getString("msg");
    }

    public static void shareText(Activity activity, Fragment fragment, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        activity.startActivity(shareIntent);
    }

   /* public static void setBgChatData(Context context, Activity activity, AdapterTicketChatBinding binding, String sender
            , String type, String message, String url, String dateTime, SimpleDateFormat simpleDateFormat) {

        MyPrefs myPrefs = MyPrefs.getInstance();
        int screenWidth = CommonFunctions.getScreenSize(activity).get("width");
        int minWidthImage = screenWidth / 2, maxWidthText = (int) (screenWidth * 0.8);
        Drawable
                drawableOther = ContextCompat.getDrawable(context, R.drawable.curve_left_corner_bg),
                drawableSelf = ContextCompat.getDrawable(context, R.drawable.curve_right_corner_bg);
        if ("user".equals(sender.toLowerCase())) {
            //binding.textViewMessage.setGravity(END);
            //binding.textViewMessageFile.setGravity(END);
            //binding.textViewTime.setGravity(START);
            //binding.textViewTimeFile.setGravity(START);
            binding.spaceRight.setVisibility(GONE);
            binding.spaceLeft.setVisibility(VISIBLE);
            binding.layoutChatBg.setBackground(drawableSelf);
            binding.textViewSender.setText("You");
            binding.textViewSender.setGravity(END);
        } else {
            // binding.textViewMessage.setGravity(START);
            //binding.textViewMessageFile.setGravity(START);
            // binding.textViewTime.setGravity(END);
            //binding.textViewTimeFile.setGravity(END);
            binding.spaceRight.setVisibility(VISIBLE);
            binding.spaceLeft.setVisibility(GONE);
            binding.layoutChatBg.setBackground(drawableOther);
            binding.textViewSender.setText(context.getString(R.string.app_name));
            binding.textViewSender.setGravity(START);
        }

        if (type.equalsIgnoreCase("text")) {
            binding.textViewMessage.setVisibility(VISIBLE);
            binding.layoutMedia.setVisibility(GONE);
            binding.textViewMessage.setMaxWidth(maxWidthText);
        } else {
            binding.textViewMessage.setVisibility(GONE);
            binding.textViewMessage.setMaxWidth(minWidthImage);
            binding.layoutMedia.setVisibility(VISIBLE);

            if (type.equalsIgnoreCase("image")) {
                binding.imageViewChat.setVisibility(VISIBLE);
                binding.textViewDoc.setVisibility(GONE);
                Glide.with(context).load(url).placeholder(R.drawable.square_placeholder).override(minWidthImage).centerCrop().into(binding.imageViewChat);
            } else {
                binding.imageViewChat.setVisibility(GONE);
                binding.textViewDoc.setVisibility(VISIBLE);
            }
        }

        binding.textViewMessage.setText(message);
        String chatDate = DataFormats.convertDate(dateTime, simpleDateFormat, DataFormats.dateFormatNativeDate),
                chatTime = DataFormats.convertDate(dateTime, simpleDateFormat, DataFormats.dateFormatNativeTime);
        binding.textViewTime.setText(*//*chatDate + SPACE +*//* chatTime);

        binding.textViewDate.setText(DataFormats.getTodayYesterday(chatDate));

    }*/






  /*  public static void openChatFileMenu(final Activity activity, final Fragment fragment, IncludeTicketChatReplyBinding binding, final int[] code) {

        PopupMenu popupMenu = new PopupMenu(activity, binding.imageViewAttachment);
        popupMenu.inflate(R.menu.menu_chat);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_image) {
                    Intent takePictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takePictureIntent.setType("image/*");

                    if (fragment != null)

                        fragment.startActivityForResult(takePictureIntent, code[0]);
                    else activity.startActivityForResult(takePictureIntent, code[0]);
                }
                if (itemId == R.id.menu_video) {
                    Intent takeVideoIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    takeVideoIntent.setType("video/mp4");

                    if (fragment != null)

                        fragment.startActivityForResult(takeVideoIntent, code[1]);
                    else activity.startActivityForResult(takeVideoIntent, code[1]);
                }
                if (itemId == R.id.menu_doc) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    String[] mimeTypes = {
                            *//*"image/jpg",
                            "image/jpeg",
                            "image/png",
                            "video/mp4",*//*
                            "application/pdf",
                            "application/msword",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    };*/
       /* String[] mimeTypes = {
                "image/jpg",
                "image/jpeg",
                "image/png"
        };*/
    //     intent.setType("*/*");
                   /* intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    if (fragment != null)

                        fragment.startActivityForResult(intent, code[2]);
                    else activity.startActivityForResult(intent, code[2]);
                }
                return true;
            }
        });
        popupMenu.show();
    }
*/

   /* public static void viewImage(Context context, String image) {
        Intent intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra("Image", image);
        context.startActivity(intent);
    }

    public static void gotoChat(Context context) {
        Intent intent = new Intent(context, FirebaseChatActivity.class);

        context.startActivity(intent);
    }*/

    public static String getDrivingMode(String vehicleType) {
        if (vehicleType.equalsIgnoreCase("auto")) {
            return "driving";
        } else if (vehicleType.equalsIgnoreCase("bike")) {
            return "bicycle";
        }
        return "driving";
    }

    /*public static void errorDialog(Activity activity, String error) {
        DialogErrorBinding errorBinding = DialogErrorBinding.inflate(activity.getLayoutInflater(), null, false);
        final AlertDialog alertDialog = DialogUtils.getCustomAlertDialog(activity, errorBinding.getRoot());
        errorBinding.textViewMessage.setText(error);
        errorBinding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }*/

  /*  public static Intent getUpIntent(Activity activity) {
        Intent intent;
        String role = MyPrefs.getInstance().getString("role");
        if (role.equalsIgnoreCase("customer")) {
            intent = new Intent(activity, CustomerHomeActivity.class);
        } else if (role.equalsIgnoreCase("pilot")) {
            intent = new Intent(activity, PilotHomeActivity.class);
        } else {
            intent = new Intent(activity, AgentHomeActivity.class);
        }
        intent.setFlags(APPConstants.clearActivities);
        return intent;
    }

    public static BitmapDescriptor getMarkerIcon(int icon) {
        *//*int height = 108;
        int width = 108;
        Bitmap b = BitmapFactory.decodeResource(App.getInstance().getResources(), icon);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);*//*
        return BitmapDescriptorFactory.fromResource(icon);
    }*/

    public static void vibrate() {

    }

   /* public static void loadImageFamilyImages(Context context, String path, ActivityBusViewProfileBinding addBalanceBinding) {
        if (!path.equalsIgnoreCase(""))
            Glide.with(context).load(path).error(R.drawable.edit_img1).into(addBalanceBinding.imageViewProfile);
        else
            Glide.with(context).load(R.drawable.edit_img1).into(addBalanceBinding.imageViewProfile);
    }*/
}
