import SignUpPage from "@/components/Signup";
import React from "react";
import NotLoggedInWrapper from "@/utils/NotLoggedInWrapper";

const page = () => {
  return (
    <NotLoggedInWrapper>
      <div>
        <SignUpPage />
      </div>
    </NotLoggedInWrapper>
  );
};

export default page;
