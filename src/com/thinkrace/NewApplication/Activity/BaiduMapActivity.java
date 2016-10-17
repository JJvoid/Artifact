package com.thinkrace.NewApplication.Activity;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.thingrace.newapplication.R;

public class BaiduMapActivity extends Activity {
	// 定位
	LocationClient mLocationClient;
	public MyLocationListener myListener = new MyLocationListener();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;
	boolean isFirstLoc = true;
	private LatLng ll;
	private LatLng llA;
	private LatLng llB;

	public Button location_l;
	public Button location_model;
	public Button mMarker;
	public CheckBox mCircle;
	private TextView locInfo;
	private Marker mMarker_blue;
	private Marker mMarker_green;

	private DecimalFormat df;
	// private Overlay myOverlay;
	private OverlayOptions ooCircle1;
	private OverlayOptions ooCircle2;

	BitmapDescriptor bd_blue = BitmapDescriptorFactory
			.fromResource(R.drawable.device_location_gps);
	BitmapDescriptor bd_green = BitmapDescriptorFactory
			.fromResource(R.drawable.device_location_lbs);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_baidu);

		// 保留两位小数
		df = new DecimalFormat("#.###");

		location_l = (Button) findViewById(R.id.location_l);
		location_model = (Button) findViewById(R.id.location_model);
		mMarker = (Button) findViewById(R.id.marker);

		mCurrentMode = LocationMode.NORMAL;
		location_model.setText("普通");
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					location_model.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					location_model.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					location_model.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				default:
					break;
				}
			}
		};
		location_model.setOnClickListener(btnClickListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap.setMapStatus(msu);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(10000);
		mLocationClient.setLocOption(option);
//		mLocationClient.start();


		location_l.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLocationClient.start();
				clearOverlay(null);
				initOverlay();
			}
		});

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				Button button = new Button(getApplicationContext());
				button.setBackgroundResource(R.drawable.devicehistory_popviewbg);
				OnInfoWindowClickListener listener = null;
				if (marker == mMarker_blue) {
					LatLng N_ll = marker.getPosition();
					button.setText("当前所在点:" + df.format(N_ll.latitude) + ","
							+ df.format(N_ll.longitude));
					mBaiduMap.hideInfoWindow();
					mInfoWindow = new InfoWindow(button, N_ll, -100);
					mBaiduMap.showInfoWindow(mInfoWindow);
				} else if (marker == mMarker_green) {
					LatLng N_ll = marker.getPosition();
//					button.setText("目标点:" + df.format(N_ll.latitude) + ","
//							+ df.format(N_ll.longitude));
//					mBaiduMap.hideInfoWindow();
//					mInfoWindow = new InfoWindow(button, N_ll, 65);
//					mBaiduMap.showInfoWindow(mInfoWindow);
					Toast.makeText(
							getApplicationContext(),
							"目标点:" + df.format(N_ll.latitude)
									+ ","
									+ df.format(N_ll.longitude), Toast.LENGTH_LONG)
							.show();
				}
				return true;
			}
		});
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude())
					.build();
			mBaiduMap.setMyLocationData(locData);

			ll = new LatLng(location.getLatitude(), location.getLongitude());
			if (isFirstLoc) {
				isFirstLoc = false;
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(14.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
			}

		}
	}

	public void initOverlay() {

		llA = new LatLng(ll.latitude, ll.longitude);
		llB = new LatLng(ll.latitude + 0.002, ll.longitude + 0.002);
		MarkerOptions ooA = new MarkerOptions().position(llA).icon(bd_blue)
				.zIndex(9).draggable(false);
//		ooA.animateType(MarkerAnimateType.drop);
		mMarker_blue = (Marker) (mBaiduMap.addOverlay(ooA));
		MarkerOptions ooB = new MarkerOptions().position(llB).icon(bd_green)
				.zIndex(9).draggable(true);
//		ooB.animateType(MarkerAnimateType.drop);
		mMarker_green = (Marker) (mBaiduMap.addOverlay(ooB));

		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {

			@Override
			public void onMarkerDragStart(Marker marker) {
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				double dis = DistanceUtil. getDistance(llA, marker.getPosition());
				Toast.makeText(
						getApplicationContext(),
						"新位置:" + df.format(marker.getPosition().latitude)
								+ ","
								+ df.format(marker.getPosition().longitude) 
								+ "      "
								+"距离:" + df.format(dis), Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onMarkerDrag(Marker marker) {
			}
		});
	}

	public void clearOverlay(View view) {
		mBaiduMap.clear();
		mMarker_blue = null;
		mMarker_green = null;

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		bd_blue.recycle();
		bd_green.recycle();
	}

}
