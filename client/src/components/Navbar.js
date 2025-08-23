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
} from "@mantine/core";
import { IconChevronDown } from "@tabler/icons-react"; // chevron icon
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useSelector } from "react-redux";

export default function Navbar() {
  const router = useRouter();
  const [token, setToken] = useState(undefined);

  const userData = useSelector((state) => state.user.data);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    setToken(storedToken);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setToken(null);
    router.push("/signin");
  };

  return (
    <div style={{ backgroundColor: "#f9fafb" }}>
      <Paper
        radius={0}
        py={20}
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

          <Group spacing="xl">
            {token === undefined ? null : (
              <>
                {token && (
                  <>
                    {userData.role === "PATIENT" && (
                      <Text
                        weight={500}
                        style={{ color: "#1f2937", cursor: "pointer" }}
                        onClick={() => router.push("/doctors")}
                      >
                        Doctors
                      </Text>
                    )}
                    {userData.role === "PATIENT" && (
                      <Text
                        weight={500}
                        style={{ color: "#1f2937", cursor: "pointer" }}
                        onClick={() => router.push("/appointment")}
                      >
                        Book
                      </Text>
                    )}

                    <Menu shadow="xl" width={180}>
                      <Menu.Target>
                        <UnstyledButton
                          style={{
                            padding: "6px 12px",
                            borderRadius: "8px",
                            border: "1px solid #e5e7eb",
                            transition: "background-color 150ms ease",
                            display: "flex",
                            alignItems: "center",
                          }}
                          onMouseEnter={(e) =>
                            (e.currentTarget.style.backgroundColor = "#f3f4f6")
                          }
                          onMouseLeave={(e) =>
                            (e.currentTarget.style.backgroundColor =
                              "transparent")
                          }
                        >
                          <Group spacing="xs">
                            <Avatar
                              name={userData.fullname}
                              color="#1e40af"
                              radius="xl"
                            />
                            <Text weight={500}>{userData.email}</Text>
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
                  </>
                )}
                {!token && (
                  <Button
                    size="sm"
                    color="#1e40af"
                    radius="xl"
                    onClick={() => router.push("/signin")}
                  >
                    Signin
                  </Button>
                )}
              </>
            )}
          </Group>
        </Container>
      </Paper>
    </div>
  );
}
