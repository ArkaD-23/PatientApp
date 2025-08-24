"use client";

import { useEffect, useState } from "react";
import {
  Button,
  Container,
  Title,
  Text,
  Group,
  Paper,
  Avatar,
  Menu,
  UnstyledButton,
  Drawer,
  Burger,
  Stack,
} from "@mantine/core";
import { IconChevronDown } from "@tabler/icons-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import { fetchUser } from "@/lib/features/userSlice";
import { jwtDecode } from "jwt-decode";

export default function Navbar() {
  const router = useRouter();
  const [token, setToken] = useState(undefined);
  const [drawerOpened, setDrawerOpened] = useState(false);
  const dispatch = useDispatch();
  const userData = useSelector((state) => state.user.data);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    setToken(storedToken);
    if (storedToken) {
      const decoded = jwtDecode(storedToken);
      const userEmail = decoded.sub;
      dispatch(fetchUser({ email: userEmail, token: storedToken }));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken(null);
    router.push("/signin");
  };

  const navLinks = (
    <>
      {userData?.role === "PATIENT" && (
        <>
          <Text
            weight={500}
            style={{ color: "#1f2937", cursor: "pointer" }}
            onClick={() => router.push("/doctors")}
          >
            Doctors
          </Text>
          <Text
            weight={500}
            style={{ color: "#1f2937", cursor: "pointer" }}
            onClick={() => router.push("/bookslot")}
          >
            Book
          </Text>
        </>
      )}
      <Text
        weight={500}
        style={{ color: "#1f2937", cursor: "pointer" }}
        onClick={() => router.push(`/appointments/${userData?.id}`)}
      >
        Appointments
      </Text>
    </>
  );

  return (
    <div style={{ backgroundColor: "#f9fafb" }}>
      <Paper
        radius={0}
        py={16}
        withBorder
        style={{
          backgroundColor: "white",
          borderBottom: "1px solid #e5e7eb",
        }}
      >
        <Container
          size="lg"
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <Link href="/">
            <Title order={3} style={{ color: "#1e40af" }}>
              MediBridge
            </Title>
          </Link>

          <Group spacing="xl" visibleFrom="sm">
            {token && navLinks}
            {token ? (
              <Menu shadow="xl" width={180}>
                <Menu.Target>
                  <UnstyledButton
                    style={{
                      padding: "6px 12px",
                      borderRadius: "8px",
                      border: "1px solid #e5e7eb",
                      display: "flex",
                      alignItems: "center",
                    }}
                  >
                    <Group spacing="xs">
                      <Avatar
                        name={userData?.fullname}
                        color="#1e40af"
                        radius="xl"
                      />
                      <Text weight={500}>{userData?.email}</Text>
                      <IconChevronDown size={16} stroke={1.5} />
                    </Group>
                  </UnstyledButton>
                </Menu.Target>
                <Menu.Dropdown>
                  <Menu.Item color="red" onClick={handleLogout}>
                    Logout
                  </Menu.Item>
                </Menu.Dropdown>
              </Menu>
            ) : (
              <Button
                size="sm"
                color="#1e40af"
                radius="xl"
                onClick={() => router.push("/signin")}
              >
                Signin
              </Button>
            )}
          </Group>

          <Burger
            opened={drawerOpened}
            onClick={() => setDrawerOpened((o) => !o)}
            hiddenFrom="sm"
          />
        </Container>
      </Paper>

      <Drawer
        opened={drawerOpened}
        onClose={() => setDrawerOpened(false)}
        padding="md"
        size="xs"
        hiddenFrom="sm"
      >
        <Stack spacing="lg">
          {token && navLinks}
          {token ? (
            <Button color="red" radius="md" onClick={handleLogout}>
              Logout
            </Button>
          ) : (
            <Button
              color="#1e40af"
              radius="md"
              onClick={() => router.push("/signin")}
            >
              Signin
            </Button>
          )}
        </Stack>
      </Drawer>
    </div>
  );
}
