package gteamproject.shere

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageClickListener
import gteamproject.communicator.Communicator


class Home : Fragment() {
    // Communicator 인터페이스 사용을 위한 변수 선언
    private lateinit var communicator: Communicator

    // + 쉐어 추천 공간 더보기 버튼 변수 선언
    private lateinit var morePlaceBtn: Button

    // 이미지 파일 경로 리스트
    var sampleImages = listOf(
            R.drawable.paris,
            R.drawable.moscow,
            R.drawable.dubai,
            R.drawable.uk
    )

    // 메인 화면에 들어갈 슬라이드 갯수
    var carouselView = arrayOfNulls<CarouselView>(5)
    // 슬라이드 갯수에 따른 ID
    var carouselViewId = arrayOf(R.id.carouselView1, R.id.carouselView2, R.id.carouselView3, R.id.carouselView4, R.id.carouselView5)

    // 메인 화면 첫 슬라이드 사진 안에 들어갈 문구
    lateinit var insideText: TextView
    // 문구에 들어갈 리스트
    var labelsForSlides = arrayOf("paris", "moscow", "dubai", "uk")

    // 아래 프로모션 슬라이드
    lateinit var promotionSlide: CarouselView
    // 프로모션 슬라이드에 들어갈 배너
    var promotionBanner = listOf(R.drawable.promotion1, R.drawable.promotion2)

    companion object{
        const val TAG : String = "로그"

        fun newInstance() : Home {
            return Home()
        }
    }

    // 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Home_c")
    }

    // 프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "Home_a")
    }

    // 뷰가 생성되었을 때
    // 프래그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Home_v")
        val view = inflater.inflate(R.layout.home, container, false)

        // 위에 선언해둔 Communicator 초기화
        communicator = activity as Communicator

        // for-in 문으로 한번에 ID 등록하여 사용할 수 있게 해줌
        for (i in carouselView.indices) {
            // 슬라이드 활성화
            carouselView[i] = view.findViewById(carouselViewId[i])

            // 각각의 슬라이드에 들어갈 사진 갯수 설정
            carouselView[i]?.pageCount = sampleImages.size
            // 각각의 슬라이드에 들어갈 사진 설정
            carouselView[i]?.setImageListener { position, imageView ->
                imageView.setImageResource(sampleImages[position])
            }
            // 이미지를 클릭했을시 프래그먼트 이동
            carouselView[i]?.setImageClickListener(ImageClickListener {
                communicator.moveFragtoFrag()
            })
        }

        // + 쉐어 추천 공간 더보기 버튼 초기화
        morePlaceBtn = view.findViewById(R.id.morePlaceBtn)

        // 눌렀을시 프래그먼트 이동
        morePlaceBtn.setOnClickListener {
            communicator.moveFragtoFrag()
        }


        insideText = view.findViewById(R.id.insideText)

        promotionSlide = view.findViewById(R.id.promotionSlide)
        promotionSlide.pageCount = promotionBanner.size
        promotionSlide.setImageListener { position, imageView ->
            imageView.setImageResource(promotionBanner[position])
        }

        // TODO: 첫번째 슬라이드(프로모션 or 카테고리) 안에 들어가는 문구를 각각의 사진에 따라 다르게 변경해야 함

        // TODO: 각각의 슬라이드 안 locationText랑 imageButton을 눌렀을시 조건에 맞게 이동
        //  예) 서울특별시 -> 서울 목록, 경기도 -> 경기 목록

        // TODO: 각각의 슬라이드 안 locationText랑 imageButton 옆에 관심 목록 저장하는 기능 넣을지 말지?
        //       그게 아니라면 아래 부분에 '장소 등록하기' 이미지 버튼 생성

        // TODO: 각각의 슬라이드에 들어갈 사진 및 내용을 서버에서 가져와야 함

        return view
    }


}