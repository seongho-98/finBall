import React, { useState, MouseEvent, useEffect, useCallback } from "react";
import axios from "axios";
import styles from "./SecurityKeypad.module.css";
import Password from "./Certification";
import { RootState } from "../../store/store";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import Toast, {Error, Success, Celebrate} from "../../components/Toast/Toast";

const BASE_HTTP_URL = "https://j9e106.p.ssafy.io";
const PASSWORD_MAX_LENGTH = 6; // 비밀번호 입력길이 제한 설정
const PASSWORD_INPUT_TEXT = "설정할 비밀번호를 입력하세요.";
const PASSWORD_REINPUT_Text = "설정할 비밀번호를 다시 입력해주세요.";

const shuffle = (nums: number[]) => {
  // 배열 섞는 함수
  let num_length = nums.length;
  while (num_length) {
    let random_index = Math.floor(num_length-- * Math.random());
    let temp = nums[random_index];
    nums[random_index] = nums[num_length];
    nums[num_length] = temp;
  }
  return nums;
};

const SecuritySetting = () => {
  let nums_init = Array.from({ length: 10 }, (v, k) => k);
  const auth = useSelector((state: RootState) => state.auth);
  const location = useLocation();
  const navigate = useNavigate();
  const formData = location.state?.formData;

  const [nums, setNums] = useState([...nums_init, "", " "]);
  const [accessToken, setAccessToken] = useState("");
  const [isInputPassword, setIsInputPassword] = useState(false);
  const [password, setPassword] = useState("");
  const [password2, setPassword2] = useState("");
  const [message, setMessage] = useState(PASSWORD_INPUT_TEXT);

  useEffect(() => {
    let nums_random = Array.from({ length: 10 }, (v, k) => k); // 이 배열을 변경해 입력문자 변경 가능
    setNums(shuffle([...nums_random, "", ""]));
  }, []);

  useEffect(() => {
    if (password.length == PASSWORD_MAX_LENGTH && !isInputPassword) {
      setIsInputPassword(true);
      setPassword2(password);
      setMessage(PASSWORD_REINPUT_Text);
      setPassword("");
    } else if (password.length == PASSWORD_MAX_LENGTH && isInputPassword) {
      // 만약 1차 비밀번호와 2차비밀번호가 같지 않다면 다시 1차 비밀번호 입력하도록 한다.
      if (password != password2) {
        Error("1차 비밀번호와 2차 비밀번호가 같지 않습니다.");
        setIsInputPassword(false);
        setPassword2("");
        setMessage(PASSWORD_INPUT_TEXT);
        setPassword("");
      } else {
        saveMyEasyPassword();
      }
    }
  }, [password]);

  const saveMyEasyPassword = async () => {
    try {
      const updatedFormData: FormData = {
        ...formData,
        easyPassword: password2,
      };

      const requestBody = JSON.stringify(updatedFormData);

      const response = await fetch(`https://j9e106.p.ssafy.io/api/user`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: requestBody,
      });

      console.log(response);
      if (response.status === 200) {
        const responseData = await response.json();
        Success("간편 비밀번호 설정에 성공했습니다.");
        navigate("/login");
      }
    } catch (error) {
      console.error("데이터 전송 실패", error);
    }
  };

  const handlePasswordChange = useCallback(
    (num) => {
      if (password.length === PASSWORD_MAX_LENGTH) {
        return;
      }

      setPassword(password + num.toString());
    },
    [password]
  );

  const erasePasswordOne = useCallback(
    (e: MouseEvent) => {
      setPassword(
        password.slice(0, password.length === 0 ? 0 : password.length - 1)
      );
    },
    [password]
  );

  const erasePasswordAll = useCallback((e: MouseEvent) => {
    setPassword("");
    let nums_random = Array.from({ length: 10 }, (v, k) => k); // 이 배열을 변경해 입력문자 변경 가능
    setNums(shuffle([...nums_random, "", ""]));
  }, []);

  const shuffleNums = useCallback(
    (num: number) => (e: MouseEvent) => {
      // 0 ~ 9 섞어주기
      // let nums_random = Array.from({ length: 10 }, (v, k) => k) // 이 배열을 변경해 입력문자 변경 가능
      // setNums(shuffle([...nums_random,"",""]))
      handlePasswordChange(num);
    },
    [handlePasswordChange]
  );

  const onClickSubmitButton = (e: MouseEvent) => {
    // 비밀번호 제출
    if (password.length === 0) {
      Error("비밀번호를 입력 후 눌러주세요!");
    }
  };
  return (
    <>
      <Toast/>
      <Password value={password} />
      <div>{message}</div>
      <div className={styles.inputer}>
        {[
          ...nums.map((n, i) => (
            <button
              value={n}
              onClick={shuffleNums(n)}
              key={i}
              className={styles.btn}
            >
              {n}
            </button>
          )),
        ]}
      </div>

      <div>
        <button
          className="num-button"
          onClick={erasePasswordAll}
          key="eraseAll"
          className={styles.bottom_btm}
        >
          X
        </button>
        <button
          className="num-button"
          onClick={erasePasswordOne}
          key="eraseOne"
          className={styles.bottom_btm}
        >
          ←
        </button>
        <button
          type="submit"
          className={styles.bottom_btm}
          onClick={onClickSubmitButton}
        >
          제출
        </button>
      </div>
    </>
  );
};

export default SecuritySetting;
