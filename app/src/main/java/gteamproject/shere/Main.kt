package gteamproject.shere

import android.content.Intent
import android.graphics.Bitmap
import android.icu.number.IntegerWidth
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.google.android.material.bottomnavigation.BottomNavigationView
import gteamproject.communicator.Communicator
import gteamproject.shere.jdbc.INSERTimg
import gteamproject.shere.jdbc.SELECTEimg
import java.io.File
import java.io.FileOutputStream

// Communicator 인터페이스 상속
class Main : AppCompatActivity(), Communicator{

    //MAIN 변수 시작
    private lateinit var home: Home
    private lateinit var shere: Shere
    private lateinit var magazine: Magazine
    private lateinit var mygpage: Mypage
    private lateinit var plus: Plus
    //MAIN 변수 끝

    //PLUS 변수 시작
    private val PICK_IMAGE_REQUEST = 1
    var imgView: ImageView? = null
    val TAG = ""

    var inum: TextView? = null
    var fnum = ""
    var dbuserid = "sinwoo" // 나영씨랑 회원가입부분 합치면 변경
    //PLUS 변수 끝


    //MAIN 액티비티 정의
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val bottomNavigationView = findViewById<View>(R.id.bottom_nav) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(onBottomNavigationItemSelectedListener)

        home = Home.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, home).commit()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, home).commit()


    }

    private val onBottomNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.menu_home -> {
                home = Home.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, home).commit()
            }
            R.id.menu_shere -> {
                shere = Shere.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, shere).commit()
            }

            R.id.menu_magazine -> {
                magazine = Magazine.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, magazine).commit()
            }

            R.id.menu_mypage -> {
                mygpage = Mypage.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, mygpage).commit()
            }

            R.id.menu_plus -> {
                plus = Plus.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, plus).commit()
            }
        }
        true
    }

    fun upLoad(v: View){
        val intent = Intent(this, DownActivity::class.java)
        startActivity(intent)
    }
    fun galleryLoad(v: View){
        val intent = Intent(this, Ex_Gallery::class.java)
        startActivity(intent)
    }






    //PLUS 프래그먼트 관련 함수
    //PLUS
    fun loadImagefromGallery(view: View?) {

        SELECTEimg(this).execute("$dbuserid")      // DB로 ID별 최대넘버 찾기

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {

            //이미지를 하나 골랐을때
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {

                //data에서 절대경로로 이미지를 가져옴
                val uri: Uri? = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                //이미지가 한계이상(?) 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                val nh = (bitmap.height * (1024.0 / bitmap.width)).toInt()
                val scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true)

                imgView = findViewById<View>(R.id.imageView2) as ImageView
                imgView!!.setImageBitmap(scaled)

                inum = findViewById(R.id.img_num)                               // 이미지 넘버확인
                println(Integer.parseInt(inum?.text.toString()) + 1)
                fnum = (Integer.parseInt(inum?.text.toString()) + 1).toString() // 파일이름으로 쓸 넘버맥이기

                saveBitmapToJpeg(scaled)

            } else {
                Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Oops! 로딩에 오류가 있습니다.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun saveBitmapToJpeg(scaled: Bitmap) {
        val imgName = "${fnum}.png"     // 파일넘버(게시글넘버).png
        val tempFile = File(filesDir.absolutePath, imgName) //cachedir
        try {
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            scaled.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            Toast.makeText(applicationContext, "파일 저장 성공", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(applicationContext, "파일 저장 실패", Toast.LENGTH_SHORT).show()
        }
    }

    fun onUploadClick(v: View) {
        uploadWithTransferUtility("$dbuserid","$fnum")
        // useruid = 각 계정별 폴더를 만들기위해, fileNamae = 숫자를 두어 게시글 삭제시 DB에 있는
        // 사진 게시글 넘버링을 삭제하면서
        // 다시 순차대로 게시사진을 덮어씌워 기존 게시글에 대한 데이터가 지워지는(덮어씌워지는)효과를 위한 넘버링


        INSERTimg(this).execute("$dbuserid","$fnum") // 업로드시 게시판 DB 데이터 증가

        home = Home.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, home).commit()
    }

    private fun uploadWithTransferUtility(useruid: String, fileName: String) {

        val credentialsProvider = CognitoCachingCredentialsProvider(
                applicationContext,
                "ap-northeast-2:9df3836a-339e-4bb0-9249-3ed02544c68e", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        )

        TransferNetworkLossHandler.getInstance(applicationContext)

        val transferUtility = TransferUtility.builder()
                .context(applicationContext) //GSApplicationClass.getInstance()
                .defaultBucket("gshere") //Bucket_Name
                .s3Client(AmazonS3Client(credentialsProvider, Region.getRegion(Regions.AP_NORTHEAST_2)))
                .build()

        /* Store the new created Image file path */

        //val uploadObserver = transferUtility.upload("BUCKET_PATH/${fileName}", file, CannedAccessControlList.PublicRead)
        val uploadObserver = transferUtility.upload("${useruid}PATH/${fileName}", File(filesDir.absolutePath + "/${fileName}.png"))
        //${useruid}PATH/${fileName} = 스토리지에 저장될 파일경로, filesDir.absolutePath + "${useruid}/2.png" = 내 파일 경로

        //CannedAccessControlList.PublicRead 읽기 권한 추가

        // Attach a listener to the observer
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    // Handle a completed upload
                }
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("MYTAG", "UPLOAD - - ID: $id, percent done = $done")
            }

            override fun onError(id: Int, ex: Exception) {
                Log.d("MYTAG", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
            }
        })

        // If you prefer to long-poll for updates
        if (uploadObserver.state == TransferState.COMPLETED) {
            /* Handle completion */

        }
    }

    // Communicator 인터페이스에 있는 메서드
    override fun moveFragtoFrag() {
        val shere = Shere()
        // 이 메서드를 사용하는 프래그먼트는 shere 프래그먼트로 이동함
        this.supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, shere).commit()
    }
}