package com.example.android0027.MyPage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.android0027.R;

public class Fragment3 extends Fragment {

    View dialogView;

    private EditText weightEditText, heightEditText;
    private RadioGroup genderRadioGroup, ageRadioGroup;
    private ImageView bmiResultImageView;
    private TextView bmiResultTextView;
    private ScrollView scrollView;


    private ProgressBar progressBar;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment 레이아웃 inflate
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        // UI 요소 초기화

        RadioButton btnGenM = view.findViewById(R.id.btnGenM);
        RadioButton btnGenW = view.findViewById(R.id.btnGenW);


        RadioButton btnAge20 = view.findViewById(R.id.btnAge20);
        RadioButton btnAge30 = view.findViewById(R.id.btnAge30);
        RadioButton btnAge40 = view.findViewById(R.id.btnAge40);
        RadioButton btnAge50 = view.findViewById(R.id.btnAge50);
        RadioButton btnAge60 = view.findViewById(R.id.btnAge60);
        RadioButton btnAge70 = view.findViewById(R.id.btnAge70);

        bmiResultImageView = view.findViewById(R.id.bmiResIv);
        bmiResultTextView = view.findViewById(R.id.bmiResTv);
        scrollView = view.findViewById(R.id.scrollView);

        progressBar = view.findViewById(R.id.bmiPgBar);

        // 계산 버튼 클릭 이벤트 처리
        Button calculateButton = view.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView =(View) View.inflate(getContext(), R.layout.dialog_bmi,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("BMI");
                dlg.setIcon(R.drawable.armicon);
                dlg.setView(dialogView);

                dlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                weightEditText = dialogView.findViewById(R.id.weightEditText);
                                heightEditText = dialogView.findViewById(R.id.heightEditText);
                                genderRadioGroup = dialogView.findViewById(R.id.rdoGrGen);
                                ageRadioGroup = dialogView.findViewById(R.id.rdoGrAge);

                                calculateBMI();
                            }
                        });


                dlg.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });
                dlg.show();



            }
        });


        return view;
    }

// private void calculateBMI() {
// // BMI 계산 및 결과 표시 로직 구현
// // 위 MainActivity의 calculateBMI 메서드 내용을 그대로 여기에 넣으면 됩니다.
// }

    private void calculateBMI() {
        // Get weight and height
        String weightStr = weightEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();

        if (TextUtils.isEmpty(weightStr)) {
            weightEditText.setError("Please enter weight");
            weightEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(heightStr)) {
            heightEditText.setError("Please enter height");
            heightEditText.requestFocus();
            return;
        }

        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr) / 100; // convert height to meters

        // Calculate BMI
        float bmi = weight / (height * height);

        updateProgressBar(bmi);


        // 성별 가져오기
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        String gender = "";
        if (selectedGenderId != -1) {
            RadioButton selectedGenderRadioButton = dialogView.findViewById(selectedGenderId);
            gender = selectedGenderRadioButton.getText().toString();
        }

        // 연령대 가져오기
        int selectedAgeId = ageRadioGroup.getCheckedRadioButtonId();
        String age = "";
        if (selectedAgeId != -1) {
            RadioButton selectedAgeRadioButton = dialogView.findViewById(selectedAgeId);
            age = selectedAgeRadioButton.getText().toString();
        }

// // 성별 가져오기
// int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
// String gender = "";
// if (selectedGenderId != -1) {
// RadioButton selectedGenderRadioButton = getView().findViewById(selectedGenderId);
// gender = selectedGenderRadioButton.getText().toString();
// }
//
//// 연령대 가져오기
// int selectedAgeId = ageRadioGroup.getCheckedRadioButtonId();
// String age = "";
// if (selectedAgeId != -1) {
// RadioButton selectedAgeRadioButton = getView().findViewById(selectedAgeId);
// age = selectedAgeRadioButton.getText().toString();
// }

        // Display BMI result
        String bmiCategory = getBMICategory(bmi, age);
        String bmiResult = "Gender: " + gender + "\nAge Group: " + age + "\nBMI: " + String.format("%.2f", bmi) + "\nCategory: " + bmiCategory;
        bmiResultTextView.setText(bmiResult);

        setBMIResultImage(bmi,gender);

        // Display BMI category image (optional)
        // You can add images corresponding to different BMI categories and display them based on the calculated BMI
        // Example: bmiResultImageView.setImageResource(R.drawable.normal_weight_image);
        // Here, you need to add image resources named as per your categories (e.g., under res/drawable folder)

        // Scroll to the bottom to show the result
// scrollView.post(new Runnable() {
// @Override
// public void run() {
// scrollView.fullScroll(ScrollView.FOCUS_DOWN);
// }
// });
    }



    // MainActivity의 getBMICategory 메서드를 복사하여 여기에 붙여넣으세요.
    private String getBMICategory(float bmi, String age) {
        // 각 연령별 BMI 범주를 정의합니다.
        if (age.equalsIgnoreCase("20대")) {
            if (bmi < 18.5) {
                return "Underweight \n " +
                        "20대, 평균 15.1%에 해당하는 마른체형에 속합니다.\n " +
                        "적당한 지방량은 면역세포를 만들어 외부로부터 저항능력을 키울수 있습니다. \n " +
                        "꾸준한 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return "Normal weight \n" +
                        "20대, 평균 58.2%에 해당하는 정상체형에 속합니다.\n " +
                        "육안으로 보기 좋은 체형입니다.\n 음주를 즐기거나 불규칙적인 " +
                        "식사로 복부비만률이 증가할 수 있습니다.\n 꾸준한 근력운동, " +
                        "운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return " 20대, 평균 10.6%에 해당하는 과체중체형에 속합니다\n20대의 " +
                        "생활습관이 나머지 생애 기간동안 건강에 많은 영향을 미친다는 연구결과들이 있습니다.\n" +
                        " 건강은 건강할 때 지켜야 한다는 말이 있습니다.\n 꾸준한 근력운동, " +
                        " 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return "20대, 평균 11.26%에 해당하는 비만체형에 속합니다.\n" +
                        " 체중감량에 대한 스트레스 커지면 목표 도달 안되고 정신적으로는 오히려 해롭습니다.\n " +
                        "폭식 등 감정적 식욕 욕구가 생기면 억제시키고 자신만의 고유한 가치와 " +
                        "개인적인 성취를 키우면서 꾸준히 실천하세요.\n 꾸준한 저강도 유산소 운동과 " +
                        "근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "20대, 평균 2.5%에 해당하는 고도비만체형에 속합니다.\n다이어트를 " +
                        "성공하기 위해서는 식사조절, 활동량 증가, 살찌는 습관 고치기를 " +
                        "병행하는 것이 가장 효과적인 방법입니다.\n운동이 힘들다고 식사조절만으로 " +
                        "칼로리섭취를 줄이는 다이어트는 단기적인 효과는 있으나 \n 장기적으로 봤을 때는 빠진 체중이 다시 찌기도 합니다.\n 활동량, 운동량을 증가시켜 장기 계획하에 체중을 관리해야 합니다.\n 꾸준한 유산소운동과 근력운동으로 체지방은 줄이고, 균형잡힌 영양섭취, 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else {
                return "20대, 평균 2.5%에 해당하는 초고도비만체형에 속합니다. \n 최소" +
                        " 12개월이상 식이조절과 함께 활동량, 운동량을 증가시켜 장기 계획하에 " +
                        "체중을 관리해야 합니다. \n 다이어트를 성공하기 위해서는 식사조절, " +
                        "활동량 증가, 특히, 살찌는 습관 고치기를 병행하는 것이 가장 효과적인 방법입니다. \n " +
                        "운동이 힘들다고 식사조절만으로 칼로리섭취를 줄이는 다이어트는 " +
                        "단기적인 효과는 있으나 \n 장기적으로 봤을 때는 빠진 체중이 다시 찌기도 합니다.\n 활동량, " +
                        "운동량을 증가시켜 장기 계획하에 체중을 관리해야 합니다.\n꾸준한 유산소 및 근력강화 운동, " +
                        "균형잡힌 영양섭취, 충분한 휴식으로 지속적으로 체지방은 줄이고 근육량을 늘리는데 집중하세요.";
            }
        }
        else if (age.equalsIgnoreCase("30대")) {
            // 30대의 경우 BMI 범주를 정의합니다.
            if (bmi < 18.5) {
                return "30대, 평균 5.9%에 해당하는 마른체형에 속합니다.\n " +
                        "체지방과 근력이 부족한 저체중은 뼈에 체중이 실리지 않아 " +
                        "골밀도가 떨어져 골다공증 위험성을 높힙니다.\n 꾸준한 근력운동, " +
                        "운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한" +
                        " 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return "30대, 평균 56.9%에 해당하는 정상체형에 속합니다.\n 육안으로 " +
                        "보기 좋은 체형으로 주기적인 체중을 체크하여 1kg이라도 " +
                        "증가하였다면 바로 체중조절을 해야합니다. \n 1kg를 방치하면 " +
                        "복리이자 처럼 계속 증가합니다.\n 꾸준한 근력운동, 운동균형잡힌" +
                        " 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요. ";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return "30대, 평균 19.1%에 해당하는 과체중체형에 속합니다.\n 술자리를 줄이고, " +
                        "신체 활동량을 늘려야 합니다.\n포식 또는 야식하는 습관이 있다면 버려야 하고, \n " +
                        "걷기, 달리기, 계단오르기, 수영, 자전거 등의 유산소와 함께 근력운동을 병행하고\n " +
                        "건강한 생활 패턴으로 바꿔주는 것이 중요합니다. \n 꾸준한 근력운동, 운동균형잡힌 " +
                        "영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return " 30대, 평균 15%에 해당하는 비만체형에 속합니다.\n 30대 이후부터 10년 단위로 근육량이 " +
                        "3~8%씩 줄어들어 배와 허리, 팔뚝을 중심으로 나잇살이 점점 붙습니다.\n 임신, " +
                        "육아, 업무적 스트레스, 시간적 압박감 등이 다이어트를 더욱 방해합니다.\n " +
                        "날씬하고 건강한 몸매를 만들기 위해서는 근육을 키우는 운동부터 시작해야 합니다.\n" +
                        "꾸준한 저강도 유산소 운동과 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취)," +
                        " 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "30대, 평균 3.1%에 해당하는 고도비만체형에 속합니다.\n 비만, " +
                        "고도비만, 복부비만은 만병의 근원으로 체지방이 증가되면 각종 " +
                        "질병이 생기게 됩니다.\n 제2형 당뇨병, 이상지질혈증, 고혈압, 지방간, " +
                        "관상동맥질환, 월경이상, 대장암, 유방암 등이 대표적인 비만 관련 질병들입니다.\n" +
                        " 꾸준한 유산소운동과 근력운동으로 체지방은 줄이고, 균형잡힌 영양섭취, " +
                        "충분한 휴식으로 근육량을 늘리는데 집중하세요. \";\n";
            }
            else {
                return "30대, 평균 2.7%에 해당하는 초고도비만체형에 속합니다.\n " +
                        "조금만 먹고도 포만감을 느끼는 습관을 만들어야 합니다.\n 우리 뇌가 적은 " +
                        "섭취량으로도 포만감을 느끼고 깨닫는 데 까지 1년이상의 시간이 필요할 수 있습니다.\n " +
                        "먼저 비만체질을 개선하고 식습관을 교정하며 식이조절과 함께 활동량, 운동량을 증가시켜 " +
                        "장기 계획하에 체중을 관리해야 합니다. \n 꾸준한 유산소 및 근력강화 운동, 균형잡힌 " +
                        "영양섭취, 충분한 휴식으로 지속적으로 체지방은 줄이고 근육량을 늘리는데 집중하세요. ";
            }
        }
        else if (age.equalsIgnoreCase("40대")) {
            // 40대의 경우 BMI 범주를 정의합니다.
            if (bmi < 18.5) {
                return "40대, 평균 1.5%에 해당하는 마른체형에 속합니다.\n " +
                        "40대 이후부터는 매년 0.5%~1%씩 뼈가 약해집니다.\n " +
                        "꾸준한 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), " +
                        "충분한 휴식으로 근육량을 늘리는데 집중하세요. ";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return "40대, 평균 41.6%에 해당하는 정상체형에 속합니다.\n " +
                        "체중과 체질량지수가 정상범위라도 허리둘레가 증가하면 " +
                        "내장지방이 증가한 것으로 볼 수 있습니다.\n 허리둘레가 늘면 " +
                        "신체에는 특이 증상이 없더라도 건강 이상 신호로 받아들여 관리해야" +
                        " 합니다.\n 꾸준한 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 " +
                        "좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요. ";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return "40대, 평균 28.4%에 해당하는 과체중체형에 속합니다.\n" +
                        " 나잇살의 원인은 여성호르몬 감소, 식이조절, 운동이 " +
                        "이루어 지지 않기 때문입니다. 그리고 그 속도는 점점 더" +
                        " 빨라집니다.\n 꾸준한 근력운동, 운동균형잡힌 영양섭취" +
                        "(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return "40대, 평균 24.9%에 해당하는 비만체형에 속합니다.\n " +
                        " 중년의 나이에 접어드는 40대가 되면 30대에 비해 지방 " +
                        "대사 능력이 급격하게 떨어지기 시작합니다.\n 가장 먼저 " +
                        "나타나는 증상으로 어깨에서 팔뚝으로 이어지는 라인이 둔탁하게" +
                        " 바뀌고 \n 배와 허리 부분은 두루뭉술한 모양으로 변해갑니다." +
                        " 40대에 운동은 필수적입니다.\n 꾸준한 저강도 유산소 운동과" +
                        " 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "40대, 평균 3.6%에 해당하는 고도비만체형에 속합니다.\n" +
                        " 비만, 고도비만, 복부비만은 만병의 근원으로 체지방이 " +
                        "증가되면 각종 질병이 생기게 됩니다.\n 어렸을 때는 복부비만이라고" +
                        " 하더라도 척추를 고정하는 척추인대, 근육 등이 버텨 줄 수 있습니다." +
                        " \n 하지만 나이가 들며 약해지면서 허리 척추뼈 (요추)가 " +
                        "앞으로 계속 밀리게 되고, 요추가 앞으로 휘는 척추전만증이 발병하기도 " +
                        "합니다.\n 또한 척추가 비정상적으로 앞으로 휘게 되면 척추 사이에 있는" +
                        " 추간판을 압박하면 추간판이 탈출하여 \n신경을 자극하는 요추추간판탈출증" +
                        "(허리디스크)를 유발하게 됩니다.\n 꾸준한 유산소운동과 근력운동으로 체지방은" +
                        " 줄이고, 균형잡힌 영양섭취, 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else {
                return "40대, 평균2~3%에 해당하는 초고도비만체형에 속합니다.\n" +
                        " 식습관과 생활패턴들이 수차례에 걸친 반복적으로 잘못된 다이어트" +
                        " 방법은 렙틴 저항성이 높으며\n 섣부른 식이제한등 의 다이어트방법은" +
                        " 쉽게 살찌는 체질로 바뀔 수 있습니다.\n 뇌세포는 죽지만 비만세포는" +
                        " 죽지 않는다라는 말이 있습니다.\n 하지만 식이조절과 함께 활동량," +
                        " 운동량을 증가시켜 12개월이상 장기 계획하에 체중을 관리한다면 비만세포는" +
                        " 반드시 줄어들 것 입니다.\n 꾸준한 유산소 및 근력강화 운동, 균형잡힌 영양섭취," +
                        " 충분한 휴식으로 지속적으로 체지방은 줄이고 근육량을 늘리는데 집중하세요.";
            }
        }
        else if (age.equalsIgnoreCase("50대")) {
            // 50대의 경우 BMI 범주를 정의합니다.
            if (bmi < 18.5) {
                return "50대, 평균 3.4%에 해당하는 마른체형에 속합니다.\n" +
                        " 50대 이상 국민 5명중에 1명은 골다공증에 걸렸다는 통계도 나와있습니다.\n" +
                        " 여성의 경우 폐경 첫 5년간 골밀도가 급속하게 떨어지는 것을" +
                        " 방지해야 합니다.<\n 꾸준한 근력운동, 운동균형잡힌 영양섭취" +
                        "(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return "50대, 평균 35%에 해당하는 정상체형에 속합니다.\n " +
                        "폐경 전후인 50대 부터는 복부비만율이 급격히 증가합니다.\n" +
                        " 체중과 체질량지수가 정상범위라도 허리둘레가 증가하지 " +
                        "않게 관리해야 합니다.\n 꾸준한 근력운동, 운동균형잡힌 " +
                        "영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return "50대, 평균 25.8%에 해당하는 과체중체형에 속합니다.\n " +
                        "폐경기가 되면 여성 호르몬이 20~30대의 절반에도 미치지 못할 만큼 줄어듭니다." +
                        "\n 특히, 피하지방은 줄고, 복부로 지방이 모이며, 혈관에는 지방이 " +
                        "끼어 건강에 심각한 영향을 줄 수 있습니다.\n 살이 몰라보게 찌면서 근육량은 " +
                        "줄어 들고 전체적으로 탄력이 없어집니다.\n 꾸준한 근력운동, 운동균형잡힌 영양섭취" +
                        "(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return "50대, 평균 32%에 해당하는 비만체형에 속합니다.\n 폐경기가 " +
                        "되면 여성 호르몬이 20~30대의 절반에도 미치지 못할 만큼 줄어듭니다.\n " +
                        "그래서 피하지방층이 줄고 지방이 복부로 모이면서 혈관에도 " +
                        "지방이 끼어 건강에 심각한 영향을 줍니다.\n 살은 몰라보게 찌면서 " +
                        "근육은 줄어들고 전체적으로 탄력이 없어집니다.\n 무리한 운동은 관절에 " +
                        "부담을 줄 수 있기 때문에 관절에 부담이 없는 운동법으로 서서히 운동량을 " +
                        "늘려가는 것이 좋습니다.\n 꾸준한 저강도 유산소 운동과 근력운동, 운동균형잡힌" +
                        " 영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "50대, 평균 3.8%에 해당하는 고도비만체형에 속합니다.\n 고도비만의 " +
                        "경우 질병으로 분류됩니다.\n 일반적으로 다이어트가 누구에게나 힘들지만 " +
                        "고도비만은 그 무엇 보다 더 힘든 과정입니다.\n 활동량, 운동량을 증가시켜" +
                        " 장기 계획하에 체중을 관리해야 합니다.\n 건강을 위해서라도 반드시 체중감량을" +
                        " 하셔야 합니다.\n 꾸준한 유산소운동과 근력운동으로 체지방은 줄이고, 균형잡힌" +
                        " 영양섭취, 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else {
                return "50대, 평균 2~3%에 해당하는 초고도비만체형에 속합니다.\n" +
                        "갱년기를 전후로 분비되는 호르몬에 큰 교란이 있게 됩니다.\n " +
                        "'어차피 나는 살을 뺄 수 없을 거야'라는 좌절감이 가장 큰 원인입니다.\n" +
                        " 식습관과 운동은 개인의 의지로 실천 가능한 부분임을 잊지마세요.\n " +
                        "식이조절과 함께 활동량, 운동량을 증가시켜 12개월이상 장기 계획하에" +
                        " 체중을 관리하여 초고도비만에서 탈출하세요.\n 꾸준한 유산소 및 " +
                        "근력강화 운동, 균형잡힌 영양섭취, 충분한 휴식으로 지속적으로 체지방은 " +
                        "줄이고 근육량을 늘리는데 집중하세요.";
            }
        }
        else if (age.equalsIgnoreCase("60대")) {
            // 60대의 경우 BMI 범주를 정의합니다.
            if (bmi < 18.5) {
                return "60대, 평균 1.2%에 해당하는 마른체형에 속합니다.\n " +
                        " 저체중일경우 상대적으로 근육량이 적을 가능성이 높아 " +
                        "척추질환 및 골다공증이 발생할 위험이 높습니다.\n " +
                        "어느 정도의 체중을 유지하는 것이 건강에 도움이 됩니다.\n" +
                        "꾸준한 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은" +
                        " 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요. ";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return "60대, 평균 24.8%에 해당하는 정상체형에 속합니다. \n " +
                        "체중과 체질량지수가 정상범위라도 복부비만이라면 심혈관질환" +
                        " 및 당뇨 등 연관이 깊습니다. \n 허리둘레가 증가하지 않게 " +
                        "관리해야 합니다.\n 꾸준한 근력운동, 운동균형잡힌 영양섭취" +
                        "(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return "60대, 평균 10.1%에 해당하는 과체중체형에 속합니다. \n " +
                        "최소 일주일에 3~5회, 꾸준히 운동하는 것이 좋지만 무리한 " +
                        "운동은 관절에 부담을 줄 수 있습니다. \n 꾸준한 근력운동," +
                        " 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취)," +
                        " 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return "60대, 평균 10.84%에 해당하는 비만체형에 속합니다. \n" +
                        " 남성은 비만율이 감소하나, 여성은 오히려증가 여성은 " +
                        "남성보다 기초 대사량과 에너지 소모량이 적고, \n " +
                        " 시간이 갈수록 지방이 더 쉽게축적되며, 폐경 이후에는 " +
                        "여성호르몬 감소로 근육이 더 줄고 주로 지방조직이 증가합니다." +
                        "\n 갱년기는 여성분들의 인생에서 가장 중요한 시기이기도 합니다." +
                        " \n 인생의 전환점과도 같은 갱년기가 되면 호르몬 저하로 " +
                        "각종 질환에 시달리게 되고 탈모나 뱃살이 늘어나기도 합니다. " +
                        "\n 균형잡힌 영양섭취, 충분한 휴식, 꾸준한 유산소 및 근력강화" +
                        " 운동을 주기적으로 실시해 체지방은 줄이고 근육량을 늘리는데 " +
                        "집중하세요.\n 꾸준한 저강도 유산소 운동과 근력운동," +
                        " 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취)," +
                        " 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "60대, 평균 3%에 해당하는 고도비만체형에 속합니다. " +
                        "\n여성의 경우 고도비만이 심할수록 우울증도 비례하여 " +
                        "증가하는 경향이 있습니다.\n 무서운 것은 고도비만 우울증은" +
                        " 겉으로 잘 보이지 않는다는 점입니다. 고도비만의 경우 " +
                        "질병으로 분류됩니다.\n 꾸준한 유산소운동과 근력운동으로 " +
                        "체지방은 줄이고, 균형잡힌 영양섭취, 충분한 휴식으로 근육량을 " +
                        "늘리는데 집중하세요. ";
            }
            else {
                return "60대, 평균 1~2%에 해당하는 초고도비만체형에 속합니다." +
                        " \n 정상적으로 식습관 개선을 하면서 운동을 통해 감량을 " +
                        "해야합니다. \n 대사가 무너지면 영양균형이 깨집니다. \n " +
                        "식습관과 운동은 개인의 의지로 실천 가능한 부분입니다.\n" +
                        " 식이조절과 함께 활동량, 운동량을 증가시켜 12개월이상 장기 " +
                        "계획하에 체중을 관리하여 초고도비만에서 탈출하세요.\n 꾸준한 유산소 " +
                        "및 근력강화 운동, 균형잡힌 영양섭취, 충분한 휴식으로 지속적으로" +
                        " 체지방은 줄이고 근육량을 늘리는데 집중하세요.";
            }
        }
        else if (age.equalsIgnoreCase("70대 이상")) {
            // 70대 이상의 경우 BMI 범주를 정의합니다.
            if (bmi < 18.5) {
                return "70대, 평균 1.2%에 해당하는 마른체형에 속합니다.\n" +
                        " 낙상사고 많습니다. 위험을 줄이기 위해 꾸준한 근력운동," +
                        " 골밀도 향상에 좋은 \n 칼슘과 비타민D 섭취를 늘리고" +
                        " 충분한 휴식으로 약해진 뼈를 강화시키세요. \n 노인에게 " +
                        "건강은 곧 체력입니다. \n 꾸준한 근력운동, 운동균형잡힌 " +
                        "영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로" +
                        " 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 18.5 && bmi < 22.9) {
                return " 70대, 평균 43.5%에 해당하는 정상체형에 속합니다.\n" +
                        " 체중과 체질량지수가 정상범위라도 복부비만이라면 심혈관질환" +
                        " 및 당뇨 등 연관이 깊습니다.\n 허리둘레가 증가하지 " +
                        "않게 관리해야 합니다. \n 꾸준한 근력운동, 운동균형잡힌 " +
                        "영양섭취(특히, 골밀도에 좋은 영양섭취), 충분한 휴식으로 " +
                        "근육량을 늘리는데 집중하세요. ";
            }
            else if (bmi >= 23 && bmi < 24.9) {
                return "70대, 평균 6%에 해당하는 과체중체형에 속합니다.\n " +
                        "관절에 부담이 매우 크며 저강도 운동으로 서서히 운동량을" +
                        " 늘려가는 것이 좋습니다.\n 최소 일주일에 3~5회, " +
                        "꾸준히 운동하는 것이 좋지만 무리한 운동은 관절에 " +
                        "부담을 줄 수 있습니다. \n 관절에 부담이 없는 운동법으로" +
                        " 서서히 운동량을 늘려가는 것이 좋습니다.\n 꾸준한 " +
                        "근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 좋은 영양섭취)," +
                        " 충분한 휴식으로 근육량을 늘리는데 집중하세요 ";
            }
            else if (bmi >= 25 && bmi < 29.9) {
                return "70대, 평균 6%에 해당하는 비만체형에 속합니다. \n" +
                        " 고령자는 신체 활동 감소, 근육 조직의 상실, 체지방 증가," +
                        " 허리로 체지방 이동 등이 건강 문제의 위험을 증가시킵니다.\n " +
                        "고령화 될수록 비만은 건강 문제(예: 당뇨병, 암, 혈중 지방(지질)" +
                        " 이상 수치(이상지질혈증), \n 고혈압, 심부전, 관상동맥 질환 및" +
                        " 관절 장애)의 위험을 크게 증가시키고 있습니다.\n 꾸준한 저강도" +
                        " 유산소 운동과 근력운동, 운동균형잡힌 영양섭취(특히, 골밀도에 " +
                        "좋은 영양섭취), 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else if (bmi >= 30 && bmi < 34.9) {
                return "70세 이상, 평균 1~2%에 해당하는 고도비만체형에 속합니다." +
                        " \n 여성의 경우 고도비만이 심할수록 우울증도 비례해 증가하는" +
                        " 경향이 있습니다. \n 무서운 것은 고도비만 우울증은 겉으로 잘" +
                        " 보이지 않는다는 점입니다. \n 고도비만의 경우 질병으로 분류됩니다." +
                        " \n 활동량, 운동량을 증가시켜 장기 계획하에 체중을 관리해야 합니다." +
                        " \n 건강을 위해서라도 반드시 체중감량을 하셔야 합니다.\n 꾸준한 " +
                        "유산소운동과 근력운동으로 체지방은 줄이고, 균형잡힌 영양섭취," +
                        " 충분한 휴식으로 근육량을 늘리는데 집중하세요.";
            }
            else {
                return "70대, 평균 1% 이내 해당하는 초고도비만체형에 속합니다.\n정상적으로" +
                        " 식습관 개선을 하면서 운동을 통해 감량을 해야합니다.\n " +
                        "대사가 무너지면 영양균형이 깨집니다.\n 식습관과 운동은" +
                        " 개인의 의지로 실천 가능한 부분입니다. \n 식이조절과 함께 활동량," +
                        " 운동량을 증가시켜 12개월이상 장기 계획하에 체중을 관리하여 초고도비만에서 탈출하세요." +
                        "\n 꾸준한 유산소 및 근력강화 운동, 균형잡힌 영양섭취, " +
                        "충분한 휴식으로 지속적으로 체지방은 줄이고 근육량을 늘리는데 집중하세요.";
            }
        }
        return ""; // 위 조건에 해당하지 않는 경우
    }

    private void updateProgressBar(float bmi) {
        int max = 100; // 프로그레스바의 최대 값
        int progress;
        if (bmi < 18.5) {
            progress = 0; // 저체중
        } else if (bmi >= 18.5 && bmi < 22.9) {
            progress = (int) (max * 0.8); // 정상 체중
        } else if (bmi >= 23 && bmi < 24.9) {
            progress = (int) (max * 0.7); // 정상 체중
        }else if (bmi >= 25 && bmi < 29.9) {
            progress = (int) (max * 0.6); // 정상 체중
        } else if (bmi >= 30 && bmi < 34.9) {
            progress = (int) (max * 0.5); // 정상 체중
        } else {
            progress = (int) (max * 0.5); // 정상 체중
        }

        progressBar.setProgress(progress);
    }

    private void setBMIResultImage(float bmi, String gender) {
        int imageResource;
        if (bmi < 18.5) {
            if (gender.equalsIgnoreCase("MAN")) {

                imageResource = R.raw.q1; // Underweight for men
            } else {
                imageResource = R.raw.w1; // Underweight for women
            }
        } else if (bmi >= 18.5 && bmi < 22.9) {
            if (gender.equalsIgnoreCase("MAN")) {
                imageResource = R.raw.q2; // Normal weight for men
            } else {
                imageResource = R.raw.w2; // Normal weight for women
            }
        } else if (bmi >= 23 && bmi < 24.9) {
            if (gender.equalsIgnoreCase("MAN")) {
                imageResource = R.raw.q3; // Normal weight for men
            } else {
                imageResource = R.raw.w3; // Normal weight for women
            }
        } else if (bmi >= 25 && bmi < 29.9) {
            if (gender.equalsIgnoreCase("MAN")) {
                imageResource = R.raw.q4; // Normal weight for men
            } else {
                imageResource = R.raw.w4; // Normal weight for women
            }
        } else if (bmi >= 30 && bmi < 34.9) {
            if (gender.equalsIgnoreCase("MAN")) {
                imageResource = R.raw.q5; // Overweight for men
            } else {
                imageResource = R.raw.w6; // Overweight for women
            }
        } else {
            if (gender.equalsIgnoreCase("MAN")) {
                imageResource = R.raw.q6; // Obese for men
            } else {
                imageResource = R.raw.w7; // Obese for women
            }
        }
        Glide.with(Fragment3.this).load(imageResource).override(200, 200).into(bmiResultImageView);
    }





}