package com.resume.parse.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;
import com.sun.imageio.plugins.common.ImageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CMSS
 * 移动云 OCR
 */
public class OCRUtil {

    public static String user_ak;
    private static String user_sk;
    private static ECloudDefaultClient client;


    static {
        user_ak = "587a7e49c0c248738bbaa2461bccfd79";
        user_sk = "8cc840be00f24d65b70fe91cd3360959";
        Credential credential = new Credential(user_ak, user_sk);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
    }


//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//
//
//        // 表格识别
////        testForm();
////        //通用文档识别
////        textGeneral();
////        //身份证识别
////        textIdCard();
////        //自定义识别
////        testCustomVerify();
////        //手写体识别
////        testHandWriting();
////        //网络图片
//        testWebImage("D:\\Software\\tutorial\\homework\\test\\src\\main\\java\\test_images\\output_0.png");
////        //营业执照识别（标准版）
////        testBusinessLicense();
////        //营业执照识别（特殊版）
////        testEnterpriseLicense();
////        //银行卡识别
////        testBankCard();
////        //名片识别
////        testBusinessCard();
////        //文档识别
////        testWord();
////        //火车票识别
////        testTrainTicket();
////        //增值税发票识别
////        testInvoice();
////        //出租车发票识别
////        testTaxiInvoice();
////        //定额发票识别
////        testFixedInvoice();
////        //机票行程单识别
////        testFlights();
////        //过路费发票
////        testToll();
////        //混贴票据识别
////        testMixed();
////        //印章检测
////        testSealDetect();
////        //印章识别
////        testSealRecognition();
////        //车牌识别
////        testLicensePlate();
////        //行驶证识别
////        testDriving();
////        //驾驶证识别
////        testDrive();
////        //vin码识别
////        testVin();
////        //电表识别
////        testAmmeter();
////        //公式识别
////        testFormula();
////        //视频文字识别（视频上传）
////        testVideoUpload();
////        //视频文字识别（视频文字识别结果查询）
////        testVideoResult();
////        // 智能结构化接口
////        testSmartStructure();
//
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
//    }

    public static String parseImage(String url) {
        long start = System.currentTimeMillis();

        //网络图片
//        testWebImage("D:\\Software\\tutorial\\homework\\test\\src\\main\\java\\test_images\\output_0.png");
        String content = testWebImage(url);

        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return content;
    }


    /**
     * 查询视频识别结果，注意此方法需要在视频上传完之后调用
     */
    public static void testVideoResult() {

        HashMap<String, Object> params = new HashMap<>(1);
        params.put("taskId", "test1234");
        IECloudRequest videoResultRequest = OcrRequestFactory.getOcrVideoRequest("/api/ocr/v1/videoresult", null, params);
        try {
            JSONObject response = (JSONObject) client.call(videoResultRequest);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求视频上传接口
     */
    public static void testVideoUpload() {
        //待上传的文件对象
        File videoFilePart1 = new File("./test_images/video_aa");
        File videoFilePart2 = new File("./test_images/video_ab");
        File videoFilePart3 = new File("./test_images/video_ac");

        HashMap<String, Object> params1 = new HashMap<>(4);
        params1.put("taskId", "test1234");
        params1.put("sequenceNumber", 0);
        params1.put("endFlag", false);
        params1.put("fileName", "test.mp4");

        HashMap<String, Object> params2 = new HashMap<>(4);
        params2.put("taskId", "test1234");
        params2.put("sequenceNumber", 1);
        params2.put("endFlag", false);
        params2.put("fileName", "test.mp4");

        HashMap<String, Object> params3 = new HashMap<>(4);
        params3.put("taskId", "test1234");
        params3.put("sequenceNumber", 2);
        params3.put("endFlag", true);
        params3.put("fileName", "test.mp4");

        IECloudRequest videoUploadRequest1 = OcrRequestFactory.getOcrVideoRequest("/api/ocr/v1/videoupload", videoFilePart1, params1);
        IECloudRequest videoUploadRequest2 = OcrRequestFactory.getOcrVideoRequest("/api/ocr/v1/videoupload", videoFilePart2, params2);
        IECloudRequest videoUploadRequest3 = OcrRequestFactory.getOcrVideoRequest("/api/ocr/v1/videoupload", videoFilePart3, params3);

        try {
            JSONObject response = (JSONObject) client.call(videoUploadRequest1);
            System.out.println(response.toString());
            JSONObject response2 = (JSONObject) client.call(videoUploadRequest2);
            System.out.println(response2.toString());
            JSONObject response3 = (JSONObject) client.call(videoUploadRequest3);
            System.out.println(response3.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文档识别-表格识别-接收图片路径作为参数
     */
//    public static void testForm() {
//        HashMap<String, Object> formParams = new HashMap<>(4);
//        JSONObject formOptions = new JSONObject();
//        formOptions.put("preprocess", true);
//        formParams.put("options", formOptions);
//
//        //参数为图片路径
//        IECloudRequest formRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/form", "./test_images/form.jpg", formParams);
//
//        //参数为图片的base64编码
//        IECloudRequest formRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/form",
//                ImageUtil.fileToBase64("./test_images/form.jpg"), formParams);
//
//        //参数为图片的URL
//        IECloudRequest formRequestUrl = OcrRequestFactory.getOcrUrlRequest("/api/ocr/v1/form", "http://ui.51bi.com/opt/siteimg/biThread/2012/04/10/fa5f2d4c-da7b-4ab6-89e1-84b53a2dafd3_650X487.jpg", formParams);
//
//        try {
//
//            JSONObject response = (JSONObject) client.call(formRequest);
//            System.out.println(response.toString());
//
//            JSONObject responseBase64 = (JSONObject) client.call(formRequestBase64);
//            System.out.println(responseBase64.toJSONString());
//
//            JSONObject responseUrl = (JSONObject) client.call(formRequestUrl);
//            System.out.println(responseUrl.toJSONString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 通用文字识别-通用印刷体识别
//     */
//    public static void textGeneral() {
//        HashMap<String, Object> generalParams = new HashMap<>();
//        JSONObject generalOptions = new JSONObject();
//        generalOptions.put("rotate_180", true);
//        generalOptions.put("language", "zh");
//        generalParams.put("options", generalOptions);
//
//        //参数为图片路径
//        IECloudRequest generalRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/general", "./test_images/general.jpg", generalParams);
//
//        //参数为图片的base64编码
//        IECloudRequest generalRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/general",
//                ImageUtil.fileToBase64("./test_images/general.jpg"),generalParams);
//
//        try {
//            JSONObject response = (JSONObject) client.call(generalRequest);
//            System.out.println(response.toString());
//
//            JSONObject responseBase64 = (JSONObject) client.call(generalRequestBase64);
//            System.out.println(responseBase64.toJSONString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 卡证识别-身份证识别
//     */
//    public static void textIdCard() {
//
//        //参数为图片路径
//        IECloudRequest idCardRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/idcard", "./test_images/sfz.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest idCardRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/idcard",
//                ImageUtil.fileToBase64("./test_images/sfz.jpg"), null);
//        //参数为图片的URL
////        IECloudRequest idCardRequestUrl = OcrRequestFactory.getOcrUrlRequest("/api/ocr/v1/idcard", "http://10.253.51.155:10086/shenfenzheng.jpg", null);
//        try {
//            JSONObject response = (JSONObject) client.call(idCardRequest);
//            System.out.println(response.toString());
//
//            JSONObject responseBase64 = (JSONObject) client.call(idCardRequestBase64);
//            System.out.println(responseBase64.toJSONString());
//
////            JSONObject responseUrl = (JSONObject) client.call(idCardRequestUrl);
////            System.out.println(responseUrl.toJSONString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 自定义识别
//     */
//    public  static void testCustomVerify() {
//        HashMap<String, Object> customVerifyParams = new HashMap<>();
//        customVerifyParams.put("TemplateId", "71469603422732288");
//        //参数为图片路径
//        IECloudRequest requestCustomVerify = OcrRequestFactory.getOcrRequest("/api/ocr/v1/selfdefinition", "./test_images/sfz.jpg", customVerifyParams);
//        //参数为图片的base64编码
//        IECloudRequest requestCustomVerifyBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/selfdefinition",
//                ImageUtil.fileToBase64("./test_images/sfz.jpg"), customVerifyParams);
//        //参数为图片的URL参数
//        IECloudRequest requestCustomVerifyUrl = OcrRequestFactory.getOcrUrlRequest("/api/ocr/v1/selfdefinition",
//                "http://10.253.51.155:10086/shenfenzheng.jpg", customVerifyParams);
//
//        try {
//            JSONObject response = (JSONObject) client.call(requestCustomVerify);
//            System.out.println(response.toString());
//
//            JSONObject responseBase64 = (JSONObject) client.call(requestCustomVerifyBase64);
//            System.out.println(responseBase64.toString());
//
//            JSONObject responseUrl = (JSONObject) client.call(requestCustomVerifyUrl);
//            System.out.println(responseUrl.toJSONString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 手写体识别
//     */
//
//    public static void testHandWriting() {
//        HashMap<String, Object> handwritingParams = new HashMap<>();
//        JSONObject handwritingOptions = new JSONObject();
//        handwritingOptions.put("preprocess", true);
//        handwritingParams.put("options", handwritingOptions);
//        //参数为图片路径
//        IECloudRequest handWritingRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/handwriting", "./test_images/handwriting.jpg", handwritingParams);
//        //参数为图片的base64编码
//        IECloudRequest handWritingRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/handwriting",
//                ImageUtil.fileToBase64("./test_images/handwriting.jpg"), handwritingParams);
//
//        IECloudRequest handwritingRequestUrl = OcrRequestFactory.getOcrUrlRequest("/api/ocr/v1/handwriting",
//                "http://10.253.51.155:10086/shouxie.jpg", handwritingParams);
//
//
//        try {
//            JSONObject response = (JSONObject) client.call(handWritingRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(handWritingRequestBase64);
//            System.out.println(responseBase64.toString());
//            JSONObject responseUrl = (JSONObject) client.call(handwritingRequestUrl);
//            System.out.println(responseUrl.toJSONString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//

    /**
     * 网络图片
     */
    public static void testWebImage() {
        //参数为图片路径
        IECloudRequest webImageRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/webimage", "./test_images/webimage.png", null);
        //参数为图片的base64编码
        IECloudRequest webImageRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/webimage",
                fileToBase64("./test_images/webimage.png"), null);
        try {
            JSONObject response = (JSONObject) client.call(webImageRequest);

            JSONObject body = response.getJSONObject("body");
//            System.out.println(body);

            JSONArray jsonArray = body.getJSONObject("content").getJSONArray("prism_wordsInfo");

            for (Object o : jsonArray) {
                Map map = (Map) o;
//                System.out.println(o);
                System.out.println(map.get("word"));
            }


//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(webImageRequestBase64);
//            System.out.println(responseBase64.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 网络图片
     */
    public static String testWebImage(String path) {
        //参数为图片路径
        IECloudRequest webImageRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/webimage", path, null);
        //参数为图片的base64编码
//        IECloudRequest webImageRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/webimage",
//                fileToBase64("./test_images/webimage.png"), null);
        try {
            JSONObject response = (JSONObject) client.call(webImageRequest);

            JSONObject body = response.getJSONObject("body");
//            System.out.println(body);

            JSONArray jsonArray = body.getJSONObject("content").getJSONArray("prism_wordsInfo");

            StringBuilder content = new StringBuilder();
            for (Object o : jsonArray) {
                Map map = (Map) o;
                System.out.println(map.get("word"));
                content.append(map.get("word")).append("\n");
            }

            return content.toString();
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(webImageRequestBase64);
//            System.out.println(responseBase64.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 营业执照识别（标准版）
     */
//    public static void testBusinessLicense() {
//        //参数为图片路径
//        IECloudRequest businessLicenseRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/businesslicense", "./test_images/businesslicence.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest businessLicenseRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/businesslicense",
//                com.sun.imageio.plugins.common.ImageUtil.fileToBase64("./test_images/businesslicence.jpg"), null);
//        try {
//            JSONObject response = (JSONObject) client.call(businessLicenseRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(businessLicenseRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //
//    /**
//     * 营业执照识别（特殊版）
//     */
//    public static void testEnterpriseLicense() {
//        //参数为图片路径
//        IECloudRequest enterpriseLicenseRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/enterpriselicense", "./test_images/enterpriselicense.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest enterpriseLicenseRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/enterpriselicense",
//                ImageUtil.fileToBase64("./test_images/enterpriselicense.jpg"), null);
//        try {
//            JSONObject response = (JSONObject) client.call(enterpriseLicenseRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(enterpriseLicenseRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 银行卡识别
//     */
//
//    public  static void testBankCard() {
//        HashMap<String, Object> bankCardParam = new HashMap<>();
//        JSONObject bankCardOptions = new JSONObject();
//        bankCardOptions.put("ret_warncode_flag", false);
//        bankCardOptions.put("ret_border_cut_image", false);
//        bankCardOptions.put("enable_copy_check", false);
//        bankCardOptions.put("enable_reshoot_check", false);
//        bankCardOptions.put("enable_border_check", false);
//        bankCardOptions.put("enable_recognize_warn_code", false);
//        bankCardOptions.put("enable_quality_value", false);
//        bankCardParam.put("options", bankCardOptions);
//
//        //参数为图片路径
//        IECloudRequest bankCardRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/bankcard", "./test_images/bankcard.jpg", bankCardParam);
//        //参数为图片的base64编码
//        IECloudRequest bankCardRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/bankcard",
//                ImageUtil.fileToBase64("./test_images/bankcard.jpg"), bankCardParam);
//        try {
//            JSONObject response = (JSONObject) client.call(bankCardRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(bankCardRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 名片识别
//     */
//    public static void testBusinessCard() {
//        HashMap<String, Object> businessCardParam = new HashMap<>();
//        JSONObject businessCardOptions = new JSONObject();
//        businessCardOptions.put("preprocess", false);
//        businessCardOptions.put("rotate_90", false);
//        businessCardOptions.put("rotate_180", false);
//        businessCardOptions.put("logo_detect", false);
//        businessCardOptions.put("ret_image", "");
//        businessCardParam.put("options", businessCardOptions);
//
//        //参数为图片路径
//        IECloudRequest businessCardRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/businesscard", "./test_images/businesscard.jpg", businessCardParam);
//        //参数为图片的base64编码
//        IECloudRequest businessCardRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/businesscard",
//                ImageUtil.fileToBase64("./test_images/businesscard.jpg"), businessCardParam);
//
//        try {
//            JSONObject response = (JSONObject) client.call(businessCardRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(businessCardRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 文档识别
//     */
//    public static void testWord() {
//        //参数为图片路径
//        IECloudRequest wordRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/generic", "./test_images/general.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest wordRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/generic",
//                ImageUtil.fileToBase64("./test_images/general.jpg"), null);
//        try {
//            JSONObject response = (JSONObject) client.call(wordRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(wordRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 火车票识别
//     */
//    public static void testTrainTicket() {
//        //参数为图片路径
//        IECloudRequest trainTicketRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/trainticket", "./test_images/trainticket.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest trainTicketRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/trainticket",
//                ImageUtil.fileToBase64("./test_images/trainticket.jpg"), null);
//
//        try {
//            JSONObject response = (JSONObject) client.call(trainTicketRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(trainTicketRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 增值税发票识别
//     */
//    public static void testInvoice() {
//        //参数为图片路径
//        IECloudRequest invoiceRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/invoice", "./test_images/fapiao.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest invoiceRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/invoice",
//                ImageUtil.fileToBase64("./test_images/pj2.png"), null);
//
//        try {
//            JSONObject response = (JSONObject) client.call(invoiceRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(invoiceRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 出租车发票识别
//     */
//    public static void testTaxiInvoice() {
//        HashMap<String, Object> taxiParam = new HashMap<>();
//        JSONObject taxiOptions = new JSONObject();
//        taxiOptions.put("enable_pdf_recognize", false);
//        taxiOptions.put("pdf_page_index", 0);
//        //参数为图片路径
//        IECloudRequest taxiInvoiceRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/taxiinvoice", "./test_images/taxiticket.jpg", taxiParam);
//        //参数为图片的base64编码
//        IECloudRequest taxiInvoiceRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/taxiinvoice",
//                ImageUtil.fileToBase64("./test_images/taxiticket.jpg"), taxiParam);
//        try {
//            JSONObject response = (JSONObject) client.call(taxiInvoiceRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(taxiInvoiceRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 定额发票识别
//     */
//    public static void testFixedInvoice() {
//        HashMap<String, Object> taxiParam = new HashMap<>();
//        JSONObject taxiOptions = new JSONObject();
//        taxiOptions.put("enable_pdf_recognize", false);
//        taxiOptions.put("pdf_page_index", 0);
//        //参数为图片路径
//        IECloudRequest fixedInvoiceRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/fixedinvoice", "./test_images/dingeinvoicequota.jpg", taxiParam);
//        //参数为图片的base64编码
//        IECloudRequest fixedInvoiceRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/fixedinvoice",
//                ImageUtil.fileToBase64("./test_images/dingeinvoicequota.jpg"), taxiParam);
//        try {
//            JSONObject response = (JSONObject) client.call(fixedInvoiceRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(fixedInvoiceRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 机票行程单识别
//     */
//    public static void testFlights() {
//        HashMap<String, Object> flightParams = new HashMap<>();
//        JSONObject flightOptions = new JSONObject();
//        flightOptions.put("enable_pdf_recognize", false);
//        flightOptions.put("pdf_page_index", 0);
//
//        //参数为图片路径
//        IECloudRequest flightRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/flights", "./test_images/flight.jpg", flightParams);
//        //参数为图片的base64编码
//        IECloudRequest flightRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/flights",
//                ImageUtil.fileToBase64("./test_images/flight.jpg"), flightParams);
//        try {
//            JSONObject response = (JSONObject) client.call(flightRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(flightRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 过路费发票识别
//     */
//    public static void testToll() {
//        HashMap<String, Object> tollParams = new HashMap<>();
//        JSONObject tollOptions = new JSONObject();
//        tollOptions.put("enable_pdf_recognize", false);
//        tollOptions.put("pdf_page_index", 0);
//
//        //参数为图片路径
//        IECloudRequest tollRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/toll", "./test_images/toll.jpg", tollParams);
//        //参数为图片的base64编码
//        IECloudRequest tollRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/toll",
//                ImageUtil.fileToBase64("./test_images/toll.jpg"), tollParams);
//        try {
//            JSONObject response = (JSONObject) client.call(tollRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(tollRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 混贴票据识别
//     */
//    public static void testMixed() {
//        HashMap<String, Object> mixedParams = new HashMap<>();
//        JSONObject mixedOptions = new JSONObject();
//        mixedOptions.put("enable_pdf_recognize", false);
//        mixedOptions.put("pdf_page_index", 0);
//
//        //参数为图片路径
//        IECloudRequest mixedRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/mixedbills", "./test_images/mixed.jpg", mixedParams);
//        //参数为图片的base64编码
//        IECloudRequest mixedRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/mixedbills",
//                ImageUtil.fileToBase64("./test_images/mixed.jpg"), mixedParams);
//
//        try {
//            JSONObject response = (JSONObject) client.call(mixedRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(mixedRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 印章识别
//     */
//    public static void testSealRecognition() {
//        HashMap<String, Object> sealParams = new HashMap<>();
//        JSONObject sealOptions = new JSONObject();
//        sealOptions.put("enable_pdf_recognize", false);
//        sealOptions.put("pdf_page_index", 0);
//
//        //参数为图片路径
//        IECloudRequest sealRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/sealrecognition", "./test_images/seal.jpg", sealParams);
//        //参数为图片的base64编码
//        IECloudRequest sealRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/sealrecognition",
//                ImageUtil.fileToBase64("./test_images/seal.jpg"), sealParams);
//        try {
//            JSONObject response = (JSONObject) client.call(sealRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(sealRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 印章检测
//     */
    public static void testSealDetect() {
        //参数为图片路径
        IECloudRequest sealDetectRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/seal", "./test_images/yinzhang.jpg", null);
        //参数为图片的base64编码
        IECloudRequest sealDetectRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/seal",
                fileToBase64("./test_images/yinzhang.jpg"), null);
        try {
//            JSONArray response = (JSONArray) client.call(sealDetectRequest);
//            System.out.println(response.toString());
            JSONObject responseBase64 = (JSONObject) client.call(sealDetectRequestBase64);
            System.out.println(responseBase64.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//
//    /**
//     * 车牌识别
//     */
//    public static void testLicensePlate() {
//        //参数为图片路径
//        IECloudRequest licensePlateRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/licenseplate", "./test_images/license.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest licensePlateRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/licenseplate",
//                ImageUtil.fileToBase64("./test_images/license.jpg"), null);
//        try {
//            JSONObject response = (JSONObject) client.call(licensePlateRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(licensePlateRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 行驶证识别
//     */
//    public static void testDriving() {
//
//        HashMap<String, Object> drivingParams = new HashMap<>();
//        drivingParams.put("type", 0);
//        //参数为图片路径
//        IECloudRequest drivingRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/driving", "./test_images/xingshizheng.jpg", drivingParams);
//        //参数为图片的base64编码
//        IECloudRequest drivingRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/driving",
//                ImageUtil.fileToBase64("./test_images/xingshizheng.jpg"), drivingParams);
//        try {
//            JSONObject response = (JSONObject) client.call(drivingRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(drivingRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 驾驶证识别
//     */
//    public static void testDrive() {
//        HashMap<String, Object> driveParams = new HashMap<>();
//        driveParams.put("type", 0);
//        //参数为图片路径
//        IECloudRequest driveRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/drive", "./test_images/jiashizheng.jpg", driveParams);
//        //参数为图片的base64编码
//        IECloudRequest driveRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/drive",
//                ImageUtil.fileToBase64("./test_images/jiashizheng.jpg"), driveParams);
//        try {
//            JSONObject response = (JSONObject) client.call(driveRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(driveRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * vin码识别
//     */
//    public static void testVin() {
//        HashMap<String, Object> vinParams = new HashMap<>();
//        //参数为图片路径
//        IECloudRequest vinCodeRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/vincode", "./test_images/vin.jpg", vinParams);
//        //参数为图片的base64编码
//        IECloudRequest vinCodeRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/vincode",
//                ImageUtil.fileToBase64("./test_images/vin.jpg"), vinParams);
//        try {
//            JSONObject response = (JSONObject) client.call(vinCodeRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(vinCodeRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 电表识别
//     */
//    public static void testAmmeter() {
//        //参数为图片路径
//        IECloudRequest ammeterRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/ammeter", "./test_images/ammter.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest ammeterRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/ammeter",
//                ImageUtil.fileToBase64("./test_images/ammter.jpg"), null);
//        try {
//            JSONObject response = (JSONObject) client.call(ammeterRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(ammeterRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 公式识别
//     */
//    public static void testFormula() {
//        //参数为图片路径
//        IECloudRequest formulaRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/formula", "./test_images/formula.jpg", null);
//        //参数为图片的base64编码
//        IECloudRequest formulaRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/formula",
//                ImageUtil.fileToBase64("./test_images/formula.jpg"), null);
//
//        try {
//            JSONObject response = (JSONObject) client.call(formulaRequest);
//            System.out.println(response.toString());
//            JSONObject responseBase64 = (JSONObject) client.call(formulaRequestBase64);
//            System.out.println(responseBase64.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 智能结构化接口
     */
    // public static void testSmartStructure() {
    //     HashMap<String, Object> mixedParams = new HashMap<>();
    //     JSONObject mixedOptions = new JSONObject();
    //     mixedOptions.put("enable_pdf_recognize", false);
    //     mixedOptions.put("pdf_page_index", 0);
    //     //参数为图片路径
    //     IECloudRequest mixedRequest = OcrRequestFactory.getOcrRequest("/api/ocr/v1/smartstructure", "./test_images/formula.jpg", mixedParams);
    //    //参数为图片的base64编码
    //    IECloudRequest mixedRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/smartstructure",
    //            ImageUtil.fileToBase64("./test_images/formula.jpg"), mixedParams);
    //     try {
    //         JSONObject response = (JSONObject) client.call(mixedRequest);
    //         System.out.println(response.toString());
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * 将 file 转化为 Base64
     */
    public static String fileToBase64(String path) {
        File file = new File(path);
        FileInputStream inputFile;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.getEncoder().encodeToString(buffer);
        } catch (Exception e) {
            throw new RuntimeException("文件路径无效\n" + e.getMessage());
        }
    }
}
