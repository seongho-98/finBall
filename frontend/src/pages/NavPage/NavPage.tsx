import { useNavigate } from "react-router-dom";
import styles from "./NavPage.module.scss";

import { RootState } from "../../store/store";
import { useSelector } from "react-redux";

import MyInfo from "../../components/User/MyInfo"

function NavPage() {
  const navigate = useNavigate();
  const isLogged = useSelector((state: RootState) => state.logged.isLogged);

  return (
    <div>
      <MyInfo />
      <div className={styles.container}>

      <div
        onClick={() => {
          navigate("/");
        }}
      >
        <p>메인화면</p>
      </div>
      {!isLogged ? (
        <>
          <div
            onClick={() => {
              navigate("/login");
            }}
            >
            <p>로그인</p>
          </div>
          <div
            onClick={() => {
              navigate("/signup");
            }}
            >
            <p>회원가입</p>
          </div>
        </>
      ) : (
        <>
        <div className={styles.box}>
          <div className={styles.navbox}>

          <div
            onClick={() => {
              navigate("/");
            }}
          >
            <p>로그아웃</p>
          </div>
          <div
            onClick={() => {
              navigate("/company/bank");
            }}
          >
            <p>계좌조회</p>
          </div>
          <div
            onClick={() => {
              navigate("/company/card");
            }}
          >
            <p>카드조회</p>
          </div>
          <div
            onClick={() => {
              navigate("/cardView");
            }}
          >
            <p>카드결제</p>
          </div>
          </div>

          <div className={styles.navbox}>
          <div
            onClick={() => {
              navigate("/accountbook");
            }}
          >
            <p>가계부</p>
          </div>
          <div
            onClick={() => {
              navigate("/transferAccount");
            }}
            >
            <p>송금</p>
          </div>
          <div
            onClick={() => {
              navigate("/startgame");
            }}
            >
            <p>게임하러가기</p>
          </div>
          <div
            onClick={() => {
              navigate("/create/group-account");
            }}
            >
            <p>모임통장 만들기</p>
          </div>

          </div>
      </div>
        </>
      )}
      </div>
    </div>
  );
}

export default NavPage;
