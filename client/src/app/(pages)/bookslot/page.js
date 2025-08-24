import Appointment from "@/components/Appointment";
import LoggedInRequired from "@/utils/LoggedInWrapper";
import React from "react";

const page = () => {
  return (
    <LoggedInRequired>
      <div>
        <Appointment />
      </div>
    </LoggedInRequired>
  );
};

export default page;
