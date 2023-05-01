package in.msmartpayagent.network;

import com.aepssdkssz.network.model.ValidateUserRequest;
import com.aepssdkssz.network.model.ValidateUserResponse;

import java.util.List;
import java.util.Map;

import in.msmartpayagent.kyc.BaseRequest;
import in.msmartpayagent.kyc.DocumentDataRequestContainer;
import in.msmartpayagent.kyc.DocumentDataResponseContainer;
import in.msmartpayagent.kyc.DocumentTypeResponseModel;
import in.msmartpayagent.kyc.FileUploadResponse;
import in.msmartpayagent.network.model.BankCollectResponse;
import in.msmartpayagent.network.model.BillPayRequest;
import in.msmartpayagent.network.model.BillPayResponse;
import in.msmartpayagent.network.model.CreditOtpResponseContainer;
import in.msmartpayagent.network.model.CreditRefundOtpRequest;
import in.msmartpayagent.network.model.CreditRefundRequest;
import in.msmartpayagent.network.model.MainRequest;
import in.msmartpayagent.network.model.MainRequest2;
import in.msmartpayagent.network.model.MainResponse;
import in.msmartpayagent.network.model.MainResponse2;
import in.msmartpayagent.network.model.OperatorsRequest;
import in.msmartpayagent.network.model.OperatorsResponse;
import in.msmartpayagent.network.model.OtpRequest;
import in.msmartpayagent.network.model.PlanRequest;
import in.msmartpayagent.network.model.PlanResponse;
import in.msmartpayagent.network.model.RechargeRequest;
import in.msmartpayagent.network.model.aeps.AddFundSettlementBankRequest;
import in.msmartpayagent.network.model.aeps.AepsAccessKeyRequest;
import in.msmartpayagent.network.model.aeps.AepsAccessKeyResponse;
import in.msmartpayagent.network.model.aeps.FundSettlementDetailsRequest;
import in.msmartpayagent.network.model.aeps.FundSettlementDetailsResponse;
import in.msmartpayagent.network.model.aeps.FundSettlementRequest;
import in.msmartpayagent.network.model.aeps.PayworldAccessKeyRequest;
import in.msmartpayagent.network.model.aeps.PayworldAccessKeyResponse;
import in.msmartpayagent.network.model.aeps.onboard.UserRegisterRequest;
import in.msmartpayagent.network.model.aeps.onboard.UserRequest;
import in.msmartpayagent.network.model.audit.AuditResponse;
import in.msmartpayagent.network.model.audit.AuditUpdateRequest;
import in.msmartpayagent.network.model.commission.CommissionRequest;
import in.msmartpayagent.network.model.commission.CommissionResponse;
import in.msmartpayagent.network.model.dmr.AccountVerifyRequest;
import in.msmartpayagent.network.model.dmr.AccountVerifyResponse;
import in.msmartpayagent.network.model.dmr.AddBeneficiaryRequest;
import in.msmartpayagent.network.model.dmr.AddBeneficiaryResponse;
import in.msmartpayagent.network.model.dmr.BankDetailsDmrResponse;
import in.msmartpayagent.network.model.dmr.BankListDmrResponse;
import in.msmartpayagent.network.model.dmr.BankRequest;
import in.msmartpayagent.network.model.dmr.DeleteBeneRequest;
import in.msmartpayagent.network.model.dmr.MoneyTransferRequest;
import in.msmartpayagent.network.model.dmr.MoneyTransferResponse;
import in.msmartpayagent.network.model.dmr.RefundLiveStatusRequest;
import in.msmartpayagent.network.model.dmr.RefundLiveStatusResponse;
import in.msmartpayagent.network.model.dmr.SenderDetailsResponse;
import in.msmartpayagent.network.model.dmr.SenderFindRequest;
import in.msmartpayagent.network.model.dmr.SenderHistoryResponse;
import in.msmartpayagent.network.model.dmr.SenderRegisterRequest;
import in.msmartpayagent.network.model.dmr.SenderRegisterResponse;
import in.msmartpayagent.network.model.dmr.ps.PSSenderRegisterRequest;
import in.msmartpayagent.network.model.fastag.FastagFetchRequest;
import in.msmartpayagent.network.model.fastag.FastagFetchResponse;
import in.msmartpayagent.network.model.fastag.FastagOperatorResponse;
import in.msmartpayagent.network.model.fastag.FastagRechargeRequest;
import in.msmartpayagent.network.model.fastag.FastagRechargeResponse;
import in.msmartpayagent.network.model.matm.MicroInitiateTransactionData;
import in.msmartpayagent.network.model.matm.MicroInitiateTransactionRequest;
import in.msmartpayagent.network.model.matm.MicroInitiateTransactionResponse;
import in.msmartpayagent.network.model.matm.MicroPostTransactionRequest;
import in.msmartpayagent.network.model.matm.MicroPostTransactionResponse;
import in.msmartpayagent.network.model.post.OperatorResponse;
import in.msmartpayagent.network.model.user.ChangePasswordRequest;
import in.msmartpayagent.network.model.user.DistrictRequest;
import in.msmartpayagent.network.model.user.DistrictResponse;
import in.msmartpayagent.network.model.user.ForgotPasswordRequest;
import in.msmartpayagent.network.model.user.KycStatusResponse;
import in.msmartpayagent.network.model.user.KycUpdateRequest;
import in.msmartpayagent.network.model.user.LoginRequest;
import in.msmartpayagent.network.model.user.LoginResponse;
import in.msmartpayagent.network.model.user.MobileValidationRequest;
import in.msmartpayagent.network.model.user.ProfileResponse;
import in.msmartpayagent.network.model.user.ProfileUpdateRequest;
import in.msmartpayagent.network.model.user.RegisterRequest;
import in.msmartpayagent.network.model.user.StateResponse;
import in.msmartpayagent.network.model.wallet.BalHistoryResponse;
import in.msmartpayagent.network.model.wallet.BalRequest;
import in.msmartpayagent.network.model.wallet.BalanceRequest;
import in.msmartpayagent.network.model.wallet.BalanceResponse;
import in.msmartpayagent.network.model.wallet.MyEarningRequest;
import in.msmartpayagent.network.model.wallet.MyEarningResponse;
import in.msmartpayagent.network.model.wallet.TicketRequest;
import in.msmartpayagent.network.model.wallet.WalletHistoryRequest;
import in.msmartpayagent.network.model.wallet.WalletHistoryResponse;
import in.msmartpayagent.network.model.wallet.WalletSearchHistoryRequest;
import in.msmartpayagent.network.model.wallet.WalletTransferRequest;
import in.msmartpayagent.network.model.wallet.complaints.ComplaintHistoryRequest;
import in.msmartpayagent.network.model.wallet.complaints.ComplaintsResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface AppMethods {
    String VERSION = "1.0";
    String DOMAIN = "https://msmartpay.in/";

    String BASE_URL = DOMAIN + "ArpitAgentApi"+VERSION+"/resources/";


    String DSID = "DSUP33273001";
    String CLIENT = "";

    String environment = "production";

    String DMR = "EKODMR2.0";
    String LOGIN = "Login";
    String WS = "WS";
    String TH = "TH";


    String DMR_PAYSPRINT="Paysprint",DMR_LEVIN="levin";

    String FindSender="/FindSender";
    String DeleteBene="/DeleteBene";
    String RegisterSender="/RegisterSender";
    String VerifySender="/VerifySender";
    String BankDetails="/BankDetails";
    String GetBankList="/GetBankList";
    String SenderHistory="/SenderHistory";
    String AddBeneAfterVerify="/AddBeneAfterVerify";
    String AccountVerifyByBankAccountIFSC="/AccountVerifyByBankAccountIFSC";
    String InitiateTransaction="/InitiateTransaction";
    String TransStatus="/TransStatus";
    String RefundTransaction="/RefundTransaction";
    String RefundDMRConfirm="/RefundDMRConfirm";


    /******* DMT *******/

    @POST
    Call<SenderDetailsResponse> findSenderDetails(@Url String url, @Body SenderFindRequest request);

    @POST
    Call<SenderRegisterResponse> registerSender(@Url String url,@Body PSSenderRegisterRequest request);

    @POST
    Call<SenderRegisterResponse> registerSenderConfirm(@Url String url,@Body PSSenderRegisterRequest request);

    @POST
    Call<BankListDmrResponse> getBankListDmr(@Url String url,@Body BankRequest request);

    @POST
    Call<BankDetailsDmrResponse> getBankDetailsDmr(@Url String url,@Body BankRequest request);

    @POST
    Call<AddBeneficiaryResponse> addBeneficiary(@Url String url,@Body AddBeneficiaryRequest request);

    @POST
    Call<MainResponse2> deleteBeneficiary(@Url String url, @Body DeleteBeneRequest request);

    @POST
    Call<SenderHistoryResponse> senderHistory(@Url String url,@Body SenderFindRequest request);

    @POST
    Call<AccountVerifyResponse> verifyAccount(@Url String url,@Body AccountVerifyRequest request);

    @POST
    Call<MoneyTransferResponse> moneyTransfer(@Url String url,@Body MoneyTransferRequest request);

    @POST
    Call<RefundLiveStatusResponse> transStatus(@Url String url,@Body RefundLiveStatusRequest request);

    @POST
    Call<MainResponse2> refundTransaction(@Url String url,@Body RefundLiveStatusRequest request);

    @POST
    Call<MainResponse2> refundTransactionConfirm(@Url String url,@Body RefundLiveStatusRequest request);


    @POST(LOGIN + "/Login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST(LOGIN + "/ForgetPass")
    Call<MainResponse> forgetPass(@Body ForgotPasswordRequest request);

    @POST(LOGIN + "/mobileValidation")
    Call<MainResponse2> mobileValidation(@Body MobileValidationRequest request);

    @POST(LOGIN + "/OtpValidation")
    Call<MainResponse2> otpValidation(@Body MobileValidationRequest request);

    @POST(LOGIN + "/signup")
    Call<MainResponse2> register(@Body RegisterRequest request);

    @POST(LOGIN + "/ChangePass")
    Call<MainResponse> changePass(@Body ChangePasswordRequest request);

    @POST(LOGIN + "/getProfile")
    Call<ProfileResponse> getProfile(@Body MainRequest request);

    @POST(LOGIN + "/getUpdateProfile")
    Call<ProfileResponse> getUpdateProfile(@Body ProfileUpdateRequest request);

    @POST(LOGIN + "/getState")
    Call<StateResponse> getState(@Body MainRequest request);

    @POST(LOGIN + "/getStateDistrict")
    Call<DistrictResponse> getStateDistrict(@Body DistrictRequest request);

    @POST(LOGIN + "/getKycDetails")
    Call<KycStatusResponse> getKycDetails(@Body MainRequest request);

    @POST(LOGIN + "/getRequestKyc")
    Call<KycStatusResponse> updateKyc(@Body KycUpdateRequest request);

    @POST("APIResponse/plan/")
    Call<PlanResponse> getPlans(@Body PlanRequest request);

    @POST("POST/billpayOperator")
    Call<OperatorsResponse> operators(@Body OperatorsRequest request);

    @POST("POST/billpayOperatorStatus")
    Call<OperatorResponse> getOperatorDetails(@Body OperatorsRequest request);

    @POST("PRE/Recharge")
    Call<MainResponse> recharge(@Body RechargeRequest request);

    @POST("POST/Billpay")
    Call<BillPayResponse> billPay(@Body BillPayRequest request);

    /******* Wallet *******/
    @POST(WS + "/WalletBalance")
    Call<BalanceResponse> getWalletBalance(@Body BalanceRequest request);

    @POST(WS + "/CollectBankDetails")
    Call<BankCollectResponse> getBankDetails(@Body MainRequest request);

    @POST(WS + "/MyCommission")
    Call<CommissionResponse> getCommissions(@Body CommissionRequest request);

    @POST(WS + "/WalletBalRequest")
    Call<MainResponse> getWalletBalReq(@Body BalRequest request);

    @POST(WS + "/WalletBalReqDetails")
    Call<BalHistoryResponse> getWalletBalReqHistory(@Body MainRequest request);

    @POST(WS + "/TurnOver")
    Call<MyEarningResponse> getMyEarning(@Body MyEarningRequest request);

    @POST(WS + "/validateWalletId")
    Call<MainResponse2> validateWalletId(@Body WalletTransferRequest request);

    @POST(WS + "/WalletTransfer")
    Call<MainResponse2> walletTransfer(@Body WalletTransferRequest request);

    @POST(WS + "/TicketT")
    Call<MainResponse2> getTicketRequest(@Body TicketRequest request);

    @POST(WS + "/complaints")
    Call<ComplaintsResponse> getComplaints(@Body ComplaintHistoryRequest request);


    @POST(TH + "/SearchTran")
    Call<WalletHistoryResponse> searchWalletHistory(@Body WalletSearchHistoryRequest request);

    @POST(TH + "/TranHistory")
    Call<WalletHistoryResponse> getWalletHistory(@Body WalletHistoryRequest request);

    @POST(TH + "/TranHistoryByDate")
    Call<WalletHistoryResponse> getWalletHistoryByDate(@Body WalletHistoryRequest request);


    /******* Wallet *******/

    /******* Micro Atm *******/

    @POST("mpos/fetchBCDetails")
    Call<MicroInitiateTransactionResponse> getMicroAtmAccessKey(@Body MicroInitiateTransactionData request);


    /******* End Micro Atm *******/

    /******* AEPS *******/

    //Activate User
    @POST("aeps/RequestOTP")
    Call<MainResponse2> activateRequestOTP(@Body UserRequest request);

    @POST("aeps/VerifyMobile")
    Call<MainResponse2> activateVerifyOTP(@Body UserRequest request);

    @POST("aeps/UserOnboard")
    Call<MainResponse2> activateRegister(@Body UserRegisterRequest request);


    @Multipart
    @POST("aeps/ActivateService")
    Call<MainResponse2> activateService(@PartMap Map<String, RequestBody> partMap,
                                        @Part List<MultipartBody.Part> files);
    //End Activate User

    @POST("aeps/generateAccessKey")
    Call<AepsAccessKeyResponse> getAepsAccessKey(@Body AepsAccessKeyRequest request);

    @POST("APIResponse/fetchAePSBCDetails")
    Call<PayworldAccessKeyResponse> generatePayworldAccessKey(@Body PayworldAccessKeyRequest request);

    @POST("aeps/generateSSPLAccessKey")
    Call<AepsAccessKeyResponse> generateSSPLAccessKey(@Body AepsAccessKeyRequest request);

    @POST("aeps/add-bank")
    Call<MainResponse2> addFundSettlementBank(@Body AddFundSettlementBankRequest request);

    @POST("aeps/settlement-details")
    Call<FundSettlementDetailsResponse> getFundSettlementList(@Body FundSettlementDetailsRequest request);

    @POST("aeps/settlement-request")
    Call<MainResponse2> requestFundSettlement(@Body FundSettlementRequest request);
    /******* END AEPS *******/

    /******* DMT *******/

    @POST(DMR + "/FindSender")
    Call<SenderDetailsResponse> findSenderDetails(@Body SenderFindRequest request);

    @POST(DMR + "/RegisterSender")
    Call<SenderRegisterResponse> registerSender(@Body SenderRegisterRequest request);

    @POST(DMR + "/VerifySender")
    Call<SenderRegisterResponse> registerSenderConfirm(@Body SenderRegisterRequest request);

    @POST(DMR + "/GetBankList")
    Call<BankListDmrResponse> getBankListDmr(@Body BankRequest request);

    @POST(DMR + "/BankDetails")
    Call<BankDetailsDmrResponse> getBankDetailsDmr(@Body BankRequest request);

    @POST(DMR + "/AddBeneAfterVerify")
    Call<AddBeneficiaryResponse> addBeneficiary(@Body AddBeneficiaryRequest request);

    @POST(DMR + "/DeleteBene")
    Call<MainResponse2> deleteBeneficiary(@Body DeleteBeneRequest request);

    @POST(DMR + "/SenderHistory")
    Call<SenderHistoryResponse> senderHistory(@Body SenderFindRequest request);

    @POST(DMR + "/ClaimHistory")
    Call<SenderHistoryResponse> claimHistory(@Body SenderFindRequest request);

    @POST(DMR + "/AccountVerifyByBankAccountIFSC")
    Call<AccountVerifyResponse> verifyAccount(@Body AccountVerifyRequest request);

    @POST(DMR + "/InitiateTransaction")
    Call<MoneyTransferResponse> moneyTransfer(@Body MoneyTransferRequest request);

    @POST(DMR + "/TransStatus")
    Call<RefundLiveStatusResponse> transStatus(@Body RefundLiveStatusRequest request);

    @POST(DMR + "/RefundTransaction")
    Call<MainResponse2> refundTransaction(@Body RefundLiveStatusRequest request);

    @POST(DMR + "/RefundDMRConfirm")
    Call<MainResponse2> refundTransactionConfirm(@Body RefundLiveStatusRequest request);

    /****** Credit card payment   *********/
    @POST(BASE_URL + "Paysprint/ccpayment/generateotp")
    Call<CreditOtpResponseContainer> generateOtp(@Body OtpRequest request);

    @POST(BASE_URL + "Paysprint/ccpayment/paybill")
    Call<MainResponse2> requestCreditPay(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/ccpayment/resendotp")
    Call<MainResponse2> requestRefundOtp(@Body CreditRefundOtpRequest request);

    @POST(BASE_URL + "Paysprint/ccpayment/claimrefund")
    Call<MainResponse2> requestRefund(@Body CreditRefundRequest request);

    @POST(BASE_URL + "Paysprint/paytm/sendotp")
    Call<MainResponse2> getPaytmOtp(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/paytm/verifyotp")
    Call<MainResponse2> verifyPaytmOtp(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/paytm/checkout")
    Call<MainResponse2> getPaytmPay(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/fastag/operatorsList")
    Call<FastagOperatorResponse> getFastagOperator(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/finocms/generate_url")
    Call<MainResponse2> generateFinoCMCUrl(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/axisbank-utm/generateurl")
    Call<MainResponse2> generateAccountOpeningUrl(@Body MainRequest2 request);

    @POST(BASE_URL + "Paysprint/fastag/fetchConsumerDetails")
    Call<FastagFetchResponse> fetchFastagConsumerDetails(@Body FastagFetchRequest request);

    @POST(BASE_URL + "Paysprint/fastag/recharge")
    Call<FastagRechargeResponse> rechargeFastag(@Body FastagRechargeRequest request);

    /*******END DMT *******/

    /*********  MATM ********/

    @POST("matm/fingpay/user/validate")
    Call<ValidateUserResponse> fingpayValidateMatmUser(@Body ValidateUserRequest request);

    @POST("matm/fingpay/initiate")
    Call<MicroInitiateTransactionResponse> initiateFingpayMATMTransaction(@Body MicroInitiateTransactionRequest request);

    @POST("matm/fingpay/transaction/update")
    Call<MicroPostTransactionResponse> postFingpayMATMTransaction(@Body MicroPostTransactionRequest request);


    /********** MATM END ************/

    /****** KYC Documents *********/
    @POST(LOGIN + "/fetchKycDocumentType")
    Call<DocumentTypeResponseModel> fetchDocumentType(@Body BaseRequest baseRequest);

    @Multipart
    @POST(DOMAIN+"FileServer/uploadFile")
    Call<FileUploadResponse> kycUploadFile(@Part List<MultipartBody.Part> files);

    @POST(LOGIN + "/uploadKycDocument")
    Call<DocumentDataResponseContainer> kycUploadDoc(@Body DocumentDataRequestContainer requestContainer);
    @POST(LOGIN + "/fetchKycDocuments")
    Call<DocumentDataResponseContainer> fetchDocumentData(@Body BaseRequest baseRequest);

    @POST(LOGIN + "/updateKYC")
    Call<DocumentDataResponseContainer> updateKYCStatus(@Body BaseRequest baseRequest);

    @POST(WS + "/auditHistory")
    Call<AuditResponse> fetchAuditRequest(@Body BaseRequest baseRequest);

    @POST(WS + "/updateAuditDetails")
    Call<MainResponse2> updateAuditDetails(@Body AuditUpdateRequest baseRequest);
}
