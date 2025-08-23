import SignInPage from "@/components/Signin";
import NotLoggedInWrapper from "@/utils/NotLoggedInWrapper";
import React from "react";

const page = () => {
  return (
    <NotLoggedInWrapper>
      <div>
        <SignInPage />
      </div>
    </NotLoggedInWrapper>
  );
};

export default page;
