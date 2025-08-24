import AppointmentsList from '@/components/AppointmentsList'
import LoggedInRequired from '@/utils/LoggedInWrapper'
import React from 'react'

const page = () => {
  return (
    <LoggedInRequired>
      <div>
        <AppointmentsList />
      </div>
    </LoggedInRequired>
  )
}

export default page