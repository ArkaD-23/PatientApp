import DoctorsList from "@/components/DoctorsList";
import LoggedInRequired from "@/utils/LoggedInWrapper";
import React from "react";

const page = () => {
  return (
    <LoggedInRequired>
      <div>
        <DoctorsList />
      </div>
    </LoggedInRequired>
  );
};

export default page;
