import { Route, Routes } from "react-router-dom";

import PrivateRoute from "./PrivateRoute.tsx";

import Home from "../pages/Home/Home";

import Counter from "../pages/Test/Counter";

import Login from "../pages/Auth/Login";
import SignUp from "../pages/Auth/SignUp";
import SignUpCerti from "../pages/Auth/SignUpCerti";

import SignUpConfrim from "../pages/Auth/SignUpConfrim";
import SecurityKeypad from "../pages/Auth/SecurityKeypad";
import SecuritySetting from "../pages/Auth/SecuritySetting.tsx";
import CertificationNaver from "../pages/Auth/CertificationNaver";

import Pinball from "../pages/Pinball/Pinball";
import Game from "../pages/Pinball/Game";
import Game2 from "../pages/Pinball/Game2";
import Card from "../pages/Card/Card";
import AccountBook from "../pages/Pinball/AccountBook";

import BankInfo from "../pages/Bank/BankInfo";
import BankConnect from "../pages/Bank/BankConnect";
import CardInfo from "../pages/Bank/CardInfo";
import CardConnect from "../pages/Bank/CardConnect";
import Agreement from "../pages/Bank/Agreement";

import TestPage from "../pages/Test/TestPage";
import NavPage from "../pages/NavPage/NavPage";

import Chatbot from "../pages/ChatBot/ChatBot";
import Quiz from "../pages/Quiz/Quiz";
import Shop from "../pages/Shop/Shop";
import InventoryAll from "../pages/Shop/InventoryAll";
import Inventory from "../pages/Shop/Inventory";

import Transfering from "../pages/Transfer/Transfering";
import ReceivingMoney from "../pages/Transfer/Receive";
import AccountList from "../pages/Transfer/AccountList";
import AccountDetail from "../pages/Transfer/AccountDetail";
import TransferAccount from "../pages/Transfer/TransferAccount";
import FillAccount from "../pages/Transfer/FillAccount";
import TransferValue from "../pages/Transfer/TransferValue";

import GroupAccount from "../pages/GroupAccount/GroupAccount";
import CreateGroupAccount from "../pages/GroupAccount/CreateGroupAccount";
// 컴포넌트 테스트용
import InviteMember from "../components/GroupAccount/InviteMember";
import GroupAccountHistory from "../pages/GroupAccount/GroupAccountHistory";

import MyDataAuth from "../pages/Auth/MyDataAuth";
import CreateFinBallAccount from "../pages/Pinball/CreateFinBallAccount";
import CreateFinBallAccountAuth from "../pages/Auth/CreateFinBallAccountAuth";

// 모임통장 계좌이체
import TransferGroupAccount from "../components/GroupAccount/TransferGroupACcount.tsx";
import TransferValueGroupAccount from "../components/GroupAccount/TransferValueGroupAccount.tsx";
import TransferingGroupAccount from "../components/GroupAccount/TransferingGroupAccount.tsx";


function Router() {
  return (
    <>
      <Routes>
        {/* 로그인x */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/signupcerti" element={<SignUpCerti />} />

        {/* 마이데이터에 사용자 인증하는 페이지*/}
        <Route path="/my-data/auth" element={<MyDataAuth />} />
        {/* 핀볼 계좌 생성하기 전, 마이데이터 인증하는 페이지 */}
        <Route
          path="/create/finball/auth"
          element={<CreateFinBallAccountAuth />}
        />
        {/* 핀볼 계좌 생성하기 전, usage등을 선택하는 페이지 */}
        <Route
          path="/create/finball-account"
          element={<CreateFinBallAccount />}
        />

        <Route path="/pinball" element={<Pinball />} />
        <Route path="/cardView" element={<Card />} />
        <Route path="/game" element={<Game />} />
        <Route path="/game2" element={<Game2 />} />
        <Route path="/signupconfrim" element={<SignUpConfrim />} />
        <Route path="/securitykeypad" element={<SecurityKeypad />} />
        <Route path="/SecuritySetting" element={<SecuritySetting />} />
        <Route path="/certificationnaver" element={<CertificationNaver />} />
        <Route path="/accountbook" element={<AccountBook />} />
        <Route path="/navpage" element={<NavPage />} />
        <Route path="/testpage" element={<TestPage />} />

        <Route path="/counter" element={<Counter />} />
        <Route path="/pinball" element={<Pinball />} />
        <Route path="/game" element={<Game />} />

        <Route element={<PrivateRoute />}>
          <Route path="/chatbot" element={<Chatbot />} />
          <Route path="/quiz" element={<Quiz />} />
          <Route path="/shop" element={<Shop />} />
          <Route path="/inventoryAll" element={<InventoryAll />} />
          <Route path="/inventory" element={<Inventory />} />
          <Route path="/transfering" element={<Transfering />} />
          <Route path="/receive" element={<ReceivingMoney />} />
          {/* 카드,은행조회 */}
          <Route path="/company/bank" element={<BankInfo />} />
          <Route path="/bank/account" element={<BankConnect />} />
          <Route path="/company/card" element={<CardInfo />} />
          <Route path="/card" element={<CardConnect />} />
          <Route path="/agreement" element={<Agreement />} />
          {/* 계좌 이체 */}
          <Route path="/accountList" element={<AccountList />} />
          <Route path="/accountDetail" element={<AccountDetail />} />
          <Route path="/transferAccount" element={<TransferAccount />} />
          <Route path="/transferValue" element={<TransferValue />} />
          <Route path="/fillAccount" element={<FillAccount />} />
          {/* 모임통장 -> 동적 라우팅 적용 */}
          <Route path="/groupaccount/:no" element={<GroupAccount />} />
          {/* 모임통장 생성 페이지 */}
          <Route
            path="/create/group-account"
            element={<CreateGroupAccount />}
          />
          {/* 모달 테스트 (사용자 초대 모달) */}
          <Route path="/invite/group-account" element={<InviteMember />} />
          {/* 모임 통장 계좌이체 */}
          <Route
            path="/transferGroupAccount"
            element={<TransferGroupAccount />}
          />
          <Route
            path="/transferValueGroupAccount"
            element={<TransferValueGroupAccount />}
          {/* 모임 통장 거래 내역 페이지 */}
          />
          <Route
            path="/group-account/history/:no"
            element={<GroupAccountHistory />}
          />
        </Route>
        <Route
          path="/transferingGroupAccount"
          element={<TransferingGroupAccount />}
        />
      </Routes>
    </>
  );
}

export default Router;
