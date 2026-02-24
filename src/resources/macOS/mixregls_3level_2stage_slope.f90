program mixregls_3level
    use mixregls32slope, only:fileout, stage2, mls, no3, fileout2
    implicit none
    call readdef_both()
    call writedef_both()
    call readat()
    call adjustdata()
    call printdesc()
    call callmixreg()
    
    if(no3 .eq. 0) then
        call estimate_level3()
    else
        call estimate_level2()
    end if
    if(mls .eq. 1) then
        CALL SYSTEM("COPY mixregls_3level1.OUT+mixregls_3level3.OUT+mixregls_3level2.OUT " // FILEOUT)
    else
        CALL SYSTEM("COPY mixregls_3level1.OUT+mixregls_3level2.OUT " // FILEOUT)
    end if
    CALL SYSTEM("COPY mixregls_3level1.OUT+mixregls_3level_final.OUT " // FILEOUT2)

    if(stage2 .ne. 0) then
        call run_stage2()
    end if
end program mixregls_3level

subroutine readdef_both()
    use mixregls32slope
    use procedures3
    implicit none
    INTEGER :: I,j,k
    
    OPEN(1, FILE='mixregls_3level_2stage_slope.def')
    READ(1,'(18A4)') HEAD
    READ(1,'(A80)')FILEDAT
    READ(1,'(A80)')FILEprefix
    filedef = trim(fileprefix)//".def"
    fileout = trim(fileprefix)//".out"
    filedat = adjustl(filedat)
    fileout2 = trim(fileprefix)//"_stage1_final.out"

    READ(1,*) NVAR, P,R,S,t,extrars, PNINT,RNINT,SNINT,tnint,rsnoint,pv,rv,sv,tv,rsv,CONV,NQ,AQUAD,aquad3, &
        MAXIT, yMISS, ncent, ncov, RIDGEIN, discard0, discard_cutoff, mls, chol, waves, gammatrans, nreps, &
        myseed, stage2, multi2nd, slopecol, sepfile, no3

    waves = 1
    if(mls .ne. 1) mls = 0
    if(ncov > 2) ncov = 2
    if(ncov < 0) ncov = 0
! set SNINT=0 (for the error variance) if SNINT=1 AND S=0
! DO NOT ALLOW A MODEL TO BE FIT WITHOUT AN ERROR VARIANCE
    if(pnint .ne. 1) pnint = 0
    if(rnint .ne. 1) rnint = 0
    if(tnint .ne. 1) tnint = 0
    if(snint .ne. 1) snint = 0
    if(rsnoint .ne. 1) rsnoint = 0
    IF (S==0 .AND. SNINT==1) SNINT=0
    miss = 1
    if(fp_equal0(ymiss)) miss = 0
    if(mls .ne. 0) then
        mls = 1
        numloc = R + 1-rnint + rv
        nalpha = 0
    else
        numloc = 1
        nalpha = R + 1-rnint + (2+waves)*rv
    end if
    if(mls .eq. 0 .and. r+rv > 0) chol=2
    if(t+tv > 0) gammatrans = 0
    if(waves .ne. 1) waves = 0
    if(ncov .eq. 2 .and. chol .eq. 0) chol=1
    
    if(no3 .eq. 1) then
        tnint = 1
        t = 0
        tv = 0
        waves = 0
    end if
! nvar     =  number of variables
! ridgein  =  initial value of the ridge
! nq       =  number of quad pts (2 to 20)
! aquad    =  adaptive quadrature 0=no 1=yes
! maxit    =  maximum number of iterations
! idIND    =  field for Level-2 ID
! yIND     =  field for continuous outcome
! p        =  number of covariates
! r        =  number of random effect variance terms
! s        =  number of error variance terms
! pnint    =  1 if no intercept for mean model (0 otherwise)
! rnint    = 1 if no intercept for BS variance model 
! snint    = 1 if no intercept for WS variance model
! ncent    =  1 for standardizing of all RHS variables (0 for no standardizing)
   
    if(no3 .eq. 0) READ(1,*) ID2IND, id2ind3, YIND
    if(no3 .eq. 1) READ(1,*) ID2IND, YIND
    if(no3 .eq. 1) id2ind3 = id2ind
     IF (P .GE. 1) THEN
        ALLOCATE(XIND(P))
        READ(1,*) (XIND(I), I=1,P)
     END IF
        IF (R .GE. 1) THEN
        ALLOCATE(UIND(R))
        READ(1,*) (UIND(I), I=1,R)
     END IF
     IF (S .GE. 1) THEN
        ALLOCATE(WIND(S))
        READ(1,*) (WIND(I), I=1,S)
     END IF
     IF (T .GE. 1) THEN
        ALLOCATE(VIND(T))
        READ(1,*) (VIND(I), I=1,T)
     END IF
     IF (extrarS .GE. 1) THEN
        ALLOCATE(rsIND(extrarS))
        READ(1,*) (rsIND(I), I=1,extrarS)
     END IF
     nv = pv + rv + sv + tv + rsv
     if(nv > 0) then
        allocate(varind(nv))
        j = 0
        allocate(varlabel(nv))
    end if
     IF (Pv .GE. 1) THEN
        READ(1,*) (varind(I), I=1,Pv)
        j = pv
     END IF
    IF (Rv .GE. 1) THEN
        READ(1,*) (varIND(I+j), I=1,Rv)
        j = j + rv
     END IF
     IF (Sv .GE. 1) THEN
        READ(1,*) (varIND(I+j), I=1,Sv)
        j = j + sv
     END IF
     IF (Tv .GE. 1) THEN
        READ(1,*) (varIND(I+j), I=1,Tv)
        j = j + tv
     END IF
     IF (rSv .GE. 1) THEN
        READ(1,*) (varIND(I+j), I=1,rSv)
        j = j + rsv
     END IF

    POLD=P
    ROLD=R
    SOLD=S
    rsold=extrars
    TOLD=T

    p = pold + 1 - pnint + (2+waves)*pv
    r = rold + 1 - rnint + (2+waves)*rv
    s = sold + 1 - snint + (2+waves)*sv
    numrs = extrars + 1 - rsnoint + rsv
    t = told + 1 - tnint + (2+waves)*tv
    if(t > 0) then
        no3 = 0
        waves = 1
    end if
    nors = 1
    if(numrs .ge. 1) nors = 0
    !Special case, random slope term requires occasion-level variables
    if(mls .eq. 1) r = rold + 1 - rnint + rv
    
! read in the labels
     READ(1,*) YLABEL
     IF (P .GE. 1) THEN
        ALLOCATE(BLAB(P))
        if(pold > 0) READ(1,*) (BLAB(I+1-pnint), I=1,Pold)
        if(pnint .ne. 1) blab(1) = "intercept"
     END IF
     IF (R .GE. 1) THEN
        ALLOCATE(ALAB(R))
        if(rold > 0) READ(1,*) (ALAB(I+1-rnint), I=1,Rold)
        if(rnint .ne. 1) alab(1) = "intercept"
     END IF
     IF (S .GE. 1) THEN
        ALLOCATE(tLAB(S))
        if(sold > 0) READ(1,*) (tLAB(I+1-snint), I=1,Sold)
        if(snint .ne. 1) tlab(1) = "intercept"
     END IF
     IF (T .GE. 1) THEN
        ALLOCATE(gLAB(T))
        if(told > 0) READ(1,*) (gLAB(I+1-tnint), I=1,Told)
        if(tnint .ne. 1) glab(1) = "intercept"
     END IF
     IF (numrS .GE. 1) THEN
        ALLOCATE(rsLABel(numrS))
        if(rsold > 0) READ(1,*) (rsLABel(I+1-rsnoint), I=1,rSold)
        if(rsnoint .ne. 1) rslabel(1) = "intercept"
     END IF
     j = 0
     IF (Pv .GE. 1) THEN
        READ(1,*) (varlabel(I), I=1,Pv)
        j = pv
     END IF
    IF (Rv .GE. 1) THEN
        READ(1,*) (varlabel(I+j), I=1,Rv)
        j = j + rv
     END IF
     IF (Sv .GE. 1) THEN
        READ(1,*) (varlabel(I+j), I=1,Sv)
        j = j + sv
     END IF
     IF (Tv .GE. 1) THEN
        READ(1,*) (varlabel(I+j), I=1,Tv)
        j = j + tv
     END IF
     IF (rSv .GE. 1) THEN
        READ(1,*) (varlabel(I+j), I=1,rSv)
        j = j + rsv
     END IF
    ndim = numloc+numrs
    ndim2 = (ndim+1)*ndim/2
    numrs2 = numrs*(numrs+1)/2
    rr = (numloc+1)*numloc/2
    ns = ndim2 - rr
    if(ncov == 2) ns = 3
    npar = p+r+s+ns
        select case(stage2)
        case(1,3)     
            readcats = 0
        case(2,4) 
            readcats = 1
        case default
            stage2 = 0
    end select
    if(stage2 .ne. 0) then
        if(nors .eq. 1) then
            pomega = -1
            pto = -1
            read(1,*) pfixed, ptheta
        else
            read(1,*) pfixed,ptheta,pomega,pto
        end if
        nvar2 = 1+max(pfixed,0)+max(pomega,0)+max(ptheta,0)+max(pto,0)

        allocate(var2ind(nvar2))
        allocate(var2label(nvar2))
        if(readcats .eq. 1) then
            read(1,*) maxj
            allocate(icode(maxj))
            READ(1,*)(ICODE(J), J = 1,MAXJ)
        end if
        if(sepfile .eq. 1) read(1,*) filedat2
        if(sepfile .eq. 1) read(1,*) nvarsep,id2indsep
        read(1,*) var2ind(1)
        k = 1
        if (Pfixed .GE. 1) THEN
            READ(1,*) (var2ind(k+I), I=1,Pfixed)
            k = k + pfixed
        END IF
        IF (Ptheta .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Pomega)
            k = k + pomega
        END IF
        IF (Pto .GE. 1) THEN
            READ(1,*) (var2IND(k+I), I=1,Pto)
        END IF
         READ(1,*) var2label(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pfixed)
            k = k + pfixed
        END IF
        IF (Ptheta .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pomega)
            k = k + pomega
        END IF
        IF (Pto .GE. 1) THEN
            READ(1,*) (var2label(k+I), I=1,Pto)
        END IF
    end if
    CLOSE(1)
end subroutine readdef_both

subroutine writedef_both
    use mixregls32slope
    implicit none
    INTEGER :: I,j,k
    OPEN(1,FILE=FILEDEF)
    WRITE(1,'(18A4)') HEAD
    WRITE(1,'(A80)') FILEDAT
    WRITE(1,'(A80)') FILEprefix
    WRITE(1,'(16I3,E10.1E2,3i2,i6,f12.3,2i2,f6.3,i2,f6.3,4i2,2i6,8i2)') NVAR, Pold, Rold, Sold, told, &
        rsold, PNINT, RNINT, SNINT, tnint, rsnoint, pv, rv, sv, tv, rsv, CONV, NQ, AQUAD, aquad3, &
        MAXIT, yMISS, NCENT, ncov, ridgein, discard0, discard_cutoff, MLS, chol, waves, gammatrans, &
        nreps, myseed, stage2, multi2nd, slopecol, sepfile, no3
    if(no3 .eq. 0) WRITE(1,'(20I3)') ID2IND, id2ind3, YIND
    if(no3 .eq. 1) WRITE(1,'(20I3)') ID2IND, YIND
    IF (P .GE. 1) THEN
        WRITE(1,'(20I3)') (XIND(I), I=1,Pold)
    END IF
    IF (R .GE. 1) THEN
        WRITE(1,'(20I3)') (UIND(I), I=1,Rold)
    END IF
    IF (S .GE. 1) THEN
        WRITE(1,'(20I3)') (WIND(I), I=1,Sold)
    END IF
    IF (T .GE. 1) THEN
        WRITE(1,'(20I3)') (VIND(I), I=1,Told)
    END IF
    if(numrs .ge. 1) then
        write(1,'(20I3)') (rsind(i), i=1,rsold)
    end if
    j=0
     IF (Pv .GE. 1) THEN
        write(1,'(20I3)') (varind(I), I=1,Pv)
        j = pv
     END IF
    IF (Rv .GE. 1) THEN
        write(1,'(20I3)') (varIND(I+j), I=1,Rv)
        j = j + rv
     END IF
     IF (Sv .GE. 1) THEN
        write(1,'(20I3)') (varIND(I+j), I=1,Sv)
        j = j + sv
     END IF
     IF (Tv .GE. 1) THEN
        write(1,'(20I3)') (varIND(I+j), I=1,Tv)
        j = j + tv
     END IF
     IF (rSv .GE. 1) THEN
        write(1,'(20I3)') (varIND(I+j), I=1,rSv)
        j = j + rsv
     END IF

    WRITE(1,*) YLABEL
    IF (P .GE. 1) THEN
        WRITE(1,*) (BLAB(I+1-pnint), I=1,Pold)
    END IF
    IF (R .GE. 1) THEN
        WRITE(1,*) (ALAB(I+1-rnint), I=1,Rold)
    END IF
    IF (S .GE. 1) THEN
        WRITE(1,*) (tLAB(I+1-snint), I=1,Sold)
    END IF
    IF (T .GE. 1) THEN
        WRITE(1,*) (gLAB(I+1-tnint), I=1,Told)
    END IF
    if(rsold >= 1) then
        write(1,*) (rslabel(i), i=1,rsold)
    end if
     j = 0
     IF (Pv .GE. 1) THEN
        write(1,*) (varlabel(I), I=1,Pv)
        j = pv
     END IF
    IF (Rv .GE. 1) THEN
        write(1,*) (varlabel(I+j), I=1,Rv)
        j = j + rv
     END IF
     IF (Sv .GE. 1) THEN
        write(1,*) (varlabel(I+j), I=1,Sv)
        j = j + sv
     END IF
     IF (Tv .GE. 1) THEN
        write(1,*) (varlabel(I+j), I=1,Tv)
        j = j + tv
     END IF
    if(rsv > 1) then
        write(1,*) (varlabel(i+j), i=1,rsv)
    end if

    if(stage2 .ne. 0) then
        if(nors .eq. 1) then
            write(1,'(2i3)') pfixed, ptheta
        else
            write(1,'(4i3)') pfixed,ptheta,pomega,pto
        end if
        if(readcats .eq. 1) then
            write(1,*) maxj
            write(1,*)(ICODE(J), J = 1,MAXJ)
        end if
        if(sepfile .eq. 1) write(1,*) filedat2
        if(sepfile .eq. 1) write(1,*) nvarsep, id2indsep
        write(1,'(20I3)') var2ind(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,'(20I3)') (var2ind(k+I), I=1,Pfixed)
            k = k + pfixed
        END IF
        IF (Ptheta .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Pomega)
            k = k + pomega
        END IF
        IF (Pto .GE. 1) THEN
            write(1,'(20I3)') (var2IND(k+I), I=1,Pto)
        END IF

        write(1,*) var2label(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pfixed)
            k = k + pfixed
        END IF
        IF (Ptheta .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pomega)
            k = k + pomega
        END IF
        IF (Pto .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pto)
        END IF
    end if
    CLOSE(1)
end subroutine writedef_both

SUBROUTINE READAT()
    use mixregls32slope
    use procedures3
    implicit none

    INTEGER :: myPASS,K,ICOUNT,myindex,IDTEMP,IDOLD,hasmiss,nvartotal,&
                widtemp,k3,widold,nlevel2b,maxk3,mult10
    REAL(KIND=bigreal),ALLOCATABLE:: TEMPR(:)
    INTEGER,ALLOCATABLE :: allvarsind(:)!,temp(:)
    LOGICAL FIRST,discardi

    ALLOCATE (TEMPR(NVAR))
        nvarTotal = 1+pold+rold+sold+told+rsold+nv
        allocate(allvarsIND(nvarTotal))
        allvarsIND(1) = yind
        allvarsIND(2:pold+1) = xind(1:pold)
        allvarsIND(pold+2:pold+rold+1) = uind(1:rold)
        allvarsIND(pold+rold+2:pold+rold+sold+1) = wind(1:sold)
        allvarsIND(pold+rold+sold+2:pold+rold+sold+told+1) = vind(1:told)
        allvarsIND(pold+rold+sold+told+2:pold+rold+sold+told+rsold+1) = rsind(1:rsold)
        allvarsIND(pold+rold+sold+told+rsold+2:pold+rold+sold+told+rsold+1+nv) = varind(1:nv)
    if(discard0 .ne. 0) open(26, file=trim(fileprefix)//"_removed.dat")
    num0 = 0
    maxk3 = 1

   ! INITIALIZE
    DO myPASS = 1,2
        IF (myPASS .EQ. 2) THEN
               ALLOCATE (Y(ICOUNT))
                ALLOCATE (X(ICOUNT,P))
                ALLOCATE (U(ICOUNT,R))
                ALLOCATE (W(ICOUNT,S))
            allocate(rsvar(icount,numrs))
                ALLOCATE (V(ICOUNT,T))
                allocate (var(icount,nv))
                allocate (varavg2(nlevel2,nv))
                allocate (varavg3(nlevel3,nv))
                allocate(ids(icount))
                allocate(ids2(icount))
              Y = 0.0D0
              X = 0.0D0
              U = 0.0D0
              W = 0.0D0
              V = 0.0D0
              var = 0.0D0
              varavg2 = 0
              varavg3 = 0
              rsvar = 0
        ! IDNI has IDs and Nobs per ID
              ALLOCATE (IDNI(nlevel3,3))
              ALLOCATE (wIDNI(nlevel2,2))
              IDNI = 0
              wIDNI = 0
            mult10 = 10**(ceiling(log10(real(maxk3)))+1)
        ENDIF
   
        K     = 1
        ICOUNT= 0
        NOBS  = 0
        FIRST  = .TRUE.
        k3 = 1
        nlevel2 = 1
        nlevel2b = 1
        nlevel3 = 1
        idtemp = -1
        widtemp = -1

        ! READ IN DATA UNTIL END
        OPEN(1,ACTION='READ',FILE=FILEDAT)

        DO   ! loop forever
        
              READ(1,*,END=1999)(TEMPR(myindex),myindex=1,NVAR)
                hasmiss = 0
                IF (MISS .EQ. 1) THEN
                    do myindex = 1,nvartotal
                        IF (FP_EQUAL(tempr(allvarsIND(myindex)), YMISS)) THEN
                            hasMISS = 1
                            exit
                        END IF
                    end do
                end if
                IF (hasMISS .NE. 0) THEN
                    CYCLE  ! give up on current value and go read next one
                end if
!write(29,*) icount,nobs,first,k3,nsubj,nsubj2,i,k,idtemp,widtemp,tempr(4)
      ! QUERY FOR NEW ID AND SET PARAMETERS ACCORDINGLY

              IDTEMP = INT(TEMPR(ID2IND))
              wIDTEMP = INT(TEMPR(ID2IND3))
              ! QUERY FOR NEW ID AND SET PARAMETERS ACCORDINGLY

              IF (.NOT. FIRST) THEN 
                  ! if r=0 and rnint=1 then NO random effects 
                  
                 IF (numloc .GE. 1 .AND. IDTEMP .EQ. IDOLD) THEN
                    K     = K+1
                    if(widtemp .eq. widold) then
                        k3=k3+1
                    else
                        discardi = .FALSE.
                        if(discard0==1 .and. mypass==2 .and. waves==0) then
                            call discard(icount-k3,icount,discardi)
                        end if
                        IF (discardi .eqv. .FALSE.) THEN
                            nlevel2b = nlevel2b + 1
                            if(myPASS .EQ. 2) then
                                widni(nlevel2,1) = widold + idold*mult10
                                if(no3 .eq. 1) widni(nlevel2,1) = widold
                                widni(nlevel2,2) = k3
                            end if
                            nlevel2 = nlevel2 + 1
                        end if
                        k3 = 1
                    end if
                 ELSE
                    discardi = .FALSE.
                    if(discard0==1 .and. mypass==2) then
                        if(waves==1) then
                            call discard(icount-k,icount,discardi)
                        else
                            call discard(icount-k3,icount,discardi)
                        end if
                    end if
                        
                    if(discardi .eqv. .FALSE.) then
                        IF (myPASS .EQ. 2) THEN
                            IDNI(nlevel3,1) = IDOLD
                            IDNI(nlevel3,2) = K
                            idni(nlevel3,3) = nlevel2b
                            widni(nlevel2,1) = widold + idold*mult10
                            if(no3 .eq. 1) widni(nlevel2,1) = widold
                            widni(nlevel2,2) = k3
                        ENDIF
                        NOBS = NOBS+K
                        nlevel3     = nlevel3+1
                    else if(waves == 1) then
                        varavg2((nlevel2-nlevel2b+1):nlevel2,:) = 0
                        varavg3(nlevel3,:) = 0
                        nlevel2 = nlevel2 - nlevel2b
                        icount = icount - k
                    else
                        varavg3(nlevel3,:) = varavg3(nlevel3,:) - varavg2(nlevel2,:)
                        varavg2(nlevel2,:) = 0
                        nlevel2 = nlevel2 - 1
                        nlevel2b = nlevel2b - 1
                        icount = icount - k3
                    end if
                    K     = 1
                    nlevel2b = 1
                        k3 = 1
                     nlevel2 = nlevel2 + 1
                 ENDIF
              ENDIF

              ! PUT TEMPORARY VALUES INTO DATA VECTORS AND MATRICES

              IDOLD = IDTEMP
              ICOUNT = ICOUNT+1
              widold = widtemp
!              k = 1
!              nsubj = nsubj+1
!              nsubj2 = 0
!              k3=1

              FIRST  = .FALSE.
              IF (myPASS == 2) THEN
                    Y(icount)  = TEMPR(YIND)
                    ids(icount) = idtemp
                    ids2(icount) = widtemp
                    do myindex=1,pold
                        x(icount,myindex+1-pnint) = tempr(xind(myindex))
                    end do
                    do myindex=1,rold
                        u(icount,myindex+1-rnint) = tempr(uind(myindex))
                    end do
                    do myindex=1,sold
                        w(icount,myindex+1-snint) = tempr(wind(myindex))
                    end do
                    do myindex=1,told
                        v(icount,myindex+1-tnint) = tempr(vind(myindex))
                    end do
                    do myindex=1,rsold
                        rsvar(icount,myindex+1-rsnoint) = tempr(rsind(myindex))
                    end do
                    do myindex=1,nv
                        var(icount,myindex) = tempr(varind(myindex))
                        varavg3(nlevel3,myindex) = varavg3(nlevel3,myindex) + tempr(varind(myindex))
                        varavg2(nlevel2,myindex) = varavg2(nlevel2,myindex) + tempr(varind(myindex))
                    end do
               END IF

        END DO   ! loop back to read next line
        
    ! cleanup final entry
1999    discardi = .FALSE.
        if(discard0==1 .and. mypass==2) then
            if(waves==1) then
                call discard(icount-k,icount,discardi)
                if(discardi .eqv. .TRUE.) then
                    nlevel2 = nlevel2 - nlevel2b
                    nlevel3 = nlevel3 - 1
                    icount = icount - k
                end if
            else
                call discard(icount-k3,icount,discardi)
                if(discardi .eqv. .TRUE.) then
                    varavg3(nlevel3,:) = varavg3(nlevel3,:) - varavg2(nlevel2,:)
                    nlevel2 = nlevel2 - 1
                    nlevel2b = nlevel2b - 1
                    icount = icount -k3
                    k = k - k3
                end if
            end if
        end if
            
        if((discardi .eqv. .FALSE.) .or. (waves .eq. 0 .and. nlevel2b > 0)) then
            IF (myPASS .EQ. 2) THEN
                IDNI(nlevel3,1) = IDOLD
                IDNI(nlevel3,2) = K
                idni(nlevel3,3) = nlevel2b
                if(discardi .eqv. .FALSE.) then
                    widni(nlevel2,1) = widold + idold*mult10
                    if(no3 .eq. 1) widni(nlevel2,1) = widold
                    widni(nlevel2,2) = k3
                end if
            ENDIF
            if(k3 > maxk3) maxk3 = k3
            NOBS = NOBS+K
!            nlevel3 = nlevel3+1
        else
        end if
    if(waves .eq. 1) then
        nsubj = nlevel3
        ntclust = nlevel2
    else
        ntclust = nlevel3
        nsubj = nlevel2
    end if
    CLOSE(1)
   END DO   ! two passes, one to get size, second to read data
        do k=1,nlevel3
            varavg3(k,1:nv) = varavg3(k,1:nv)/idni(k,2)
        end do
        do k=1,nlevel2
            varavg2(k,1:nv) = varavg2(k,1:nv)/widni(k,2)
        end do
   DEALLOCATE(TEMPR)
!    do j=1,nobs
!        write(116,'(i9,36f10.3)') ids(j), y(j), (x(j,m+1-pnint),m=1,pold), &
!                (u(j,m+1-rnint),m=1,rold), (w(j,m+1-snint), m=1,sold),(var(j,m), m=1,nv)
!    end do

END SUBROUTINE READAT

subroutine adjustdata()
    use mixregls32slope
    use procedures3
    implicit none
    integer:: j,ll,kv,total1,total2,k2,k3,k1,nd,k,nt
    character(len=1) :: mylabels(3)
    
    mylabels = ["1","2","3"]
    
    if(pnint .ne. 1) x(:,1) = 1
    if(rnint .ne. 1) u(:,1) = 1
    if(snint .ne. 1) w(:,1) = 1
    if(rsnoint .ne. 1) rsvar(:,1) = 1
    if(tnint .ne. 1) v(:,1) = 1
    nd = 2+waves
    nt = 2

    kv = 0
    do j=1, pv
        ll = pold+1-pnint
        do k=1,nd
            blab(ll+(j-1)*nd+k) = trim(varlabel(j+kv)) // "_" // mylabels(k)
        end do
    end do
    kv = kv + pv
    do j=1, rv
        ll = rold + 1 - rnint
        if(mls .eq. 1) then
            alab(ll+j) = trim(varlabel(j+kv)) // "_1"
        else
            do k=1,nd
                alab(ll+(j-1)*nd+k) = trim(varlabel(j+kv)) // "_" // mylabels(k)
            end do
        end if            
    end do
    kv = kv + rv
    do j=1, sv
        ll = sold+1-snint
        do k=1,nd
            tlab(ll+(j-1)*nd+k) = trim(varlabel(j+kv)) // "_" // mylabels(k)
        end do
    end do
    kv = kv + tv
    do j=1, tv
        ll = told+1-tnint
        do k=1,nd
            glab(ll+(j-1)*nd+k) = trim(varlabel(j+kv)) // "_" // mylabels(k)
        end do
    end do
    kv = kv + rsv
    do j=1, rsv
        ll = rsold+1-rsnoint
        rslabel(ll+j) = trim(varlabel(j+kv)) // "_1"
    end do
    total1 = 0
    total2 = 0
    do k1=1,nlevel3
        do k2 = 1,idni(k1,3)
            total2 = total2 + 1
            do k3 = 1, widni(total2,2)
                total1 = total1 + 1
                kv = 0
                do j=1, pv
                    ll = pold + 1 - pnint
                    x(total1,ll+(j-1)*nd+1) = var(total1,j) - varavg2(total2,j)
                    x(total1,ll+(j-1)*nd+2) = varavg2(total2,j)
                    if(waves .eq. 1) then
                        x(total1,ll+(j-1)*nd+3) = varavg3(k1,j)
                        x(total1,ll+(j-1)*nd+2) = varavg2(total2,j) - varavg3(k1,j)
                    end if
                end do
                kv = kv + pv
                do j=1, rv
                    ll = rold + 1 - rnint
                    if(mls .eq. 1) then
    !Need to decide what to use for this!
                        u(total1,ll+j) = var(total1,j+kv) - varavg2(total2,j+kv)
                    else
                        u(total1,ll+(j-1)*nd+1) = var(total1,j) - varavg2(total2,j)
                        u(total1,ll+(j-1)*nd+2) = varavg2(total2,j)
                        if(waves .eq. 1) then
                            u(total1,ll+(j-1)*nd+3) = varavg3(k1,j)
                            u(total1,ll+(j-1)*nd+2) = varavg2(total2,j) - varavg3(k1,j)
                        end if
                    end if            
                end do
                kv = kv + rv
                do j=1, sv
                    ll = sold + 1 - snint
                    w(total1,ll+(j-1)*nd+1) = var(total1,j) - varavg2(total2,j)
                    w(total1,ll+(j-1)*nd+2) = varavg2(total2,j)
                    if(waves .eq. 1) then
                        w(total1,ll+(j-1)*nd+3) = varavg3(k1,j)
                        w(total1,ll+(j-1)*nd+2) = varavg2(total2,j) - varavg3(k1,j)
                    end if
                end do
                kv = kv + sv
                do j=1, tv
                    ll = told + 1 - tnint
                    v(total1,ll+(j-1)*nd+1) = var(total1,j) - varavg2(total2,j)
                    v(total1,ll+(j-1)*nd+2) = varavg2(total2,j)
                    if(waves .eq. 1) then
                        v(total1,ll+(j-1)*nd+3) = varavg3(k1,j)
                        v(total1,ll+(j-1)*nd+2) = varavg2(total2,j) - varavg3(k1,j)
                    end if
    !Need to decide what to use for this!
                end do
                kv = kv + tv
                do j=1, rsv
                    ll = rsold + 1 - rsnoint
                    rsvar(total1,ll+j) = var(total1,j+kv) - varavg2(total2,j+kv)
                end do
                if(nv > 0) write(116,'(i9,36f10.3)') total1,varavg3(k1,1),varavg2(total2,1),var(total1,1)
            end do
        end do
    end do
    CALL SUBMANDV()
!if(nv > 0) then
!        do k=1,nobs
!            write(116,'(i9,36f10.3)') k,(var(k,j),j=1,nv)
!        end do
!end if
end subroutine adjustData
   
SUBROUTINE PRINTDESC()
    use mixregls32slope
    use procedures3
        implicit none
        INTEGER:: IUN,I

    real(kind=10)::tempr(nobs),meany,miny,maxy,stdy

    do i=1,nobs
        write(116,'(i9,36f10.3)') i,x(i,1:p)
    end do
     meany=SUM(y(1:nobs))/DBLE(nobs)
     miny=minval(y(1:nobs))
     maxy=maxval(y(1:nobs))
     tempR(:)=0.0D0
     tempR(:)=(y(1:nobs)-meany)**2
     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
             IUN    = 16
             OPEN(UNIT=IUN,FILE="mixREGLS_3level1.OUT")
             
             WRITE(IUN,'("MIXREGLS_3level: Mixed-effects Location Scale Model")')
             write (IUN,*)
             WRITE(IUN,'("-----------------------------")')
             WRITE(IUN,'("mixREGLS_3level.DEF specifications")')
             WRITE(IUN,'("-----------------------------")')
             WRITE(IUN,"(1x,18a4)")HEAD
             WRITE(IUN,*)
             WRITE(IUN,'(" data and output files:")')
             WRITE(IUN,"(1x,a80)")FILEDAT
             WRITE(IUN,"(1x,a80)")FILEOUT
             WRITE(IUN,*)
            WRITE(IUN,"(' MULTIPLE LOCATION EFFECTS  = ',L1)") mls .eq. 1
            WRITE(IUN,"(' R  = ',I1)") rr*mls+nalpha
             WRITE(IUN,"(' NUMBER OF SCALE EFFECT(S)  = ',i1)") numrs

             WRITE(IUN,"(' CONVERGENCE CRITERION = ',F11.8)") CONV
             WRITE(IUN,"(' RIDGEIN = ',F8.4)") RIDGEIN
             WRITE(IUN,"(' NQ = ',I4)") NQ
             WRITE(IUN,"(' ADAPTIVE QUADRATURE = ',L1,' (Subject-level)')") AQUAD .eq. 1
             if(no3 .ne. 1) WRITE(IUN,"(' ADAPTIVE QUADRATURE = ',L1,' (Other level)')") AQUAD3 .eq. 1
             WRITE(IUN,"(' MAXIT = ',I4)") MAXIT
             WRITE(IUN,"(' NCOV = ',I1)") ncov
             WRITE(IUN,"(' CHOLEKSY = ',I1)") chol
             if(no3 .ne. 1) WRITE(IUN,"(' TRANSFORM GAMMA = ',l1)") gammatrans .eq. 1

             WRITE(IUN,*)
             WRITE(IUN,*)
             WRITE(IUN,'("------------")')
             WRITE(IUN,'("Descriptives")')
             WRITE(IUN,'("------------")')
             WRITE(IUN,*)
            WRITE(IUN,'(" Number of level-1 observations = ",I8)') nobs
             WRITE(IUN,*)
!            write(IUN,'(" Number of level-2 clusters     = ",I8)') nlevel2
            write(IUN,'(" Number of level-2 waves        = ",I8)') nlevel2
             write(IUN,*)
        if(no3 .ne. 1) then
!            write(IUN,'(" Number of level-3 clusters     = ",I8)') nlevel3
            write(IUN,'(" Number of level-3 subjects     = ",I8)') nlevel3
            write(IUN,*)
        end if
        if(discard0 .ne. 0) WRITE(IUN,508)num0
        508 FORMAT(//,1x,'==> The number of clusters removed because of low-varying responses =', I6)
        if(discard0 .ne. 0) write(IUN,*) '(see '//trim(fileprefix)//'_removed.dat for information about those clusters)'
        if(no3 .ne. 1) then
             write(IUN,'(" Number of level-1 observations for each level-3 cluster")')
             write(IUN,'(1x,13I6)') (IDNI(i,2), i=1,nlevel3)
             write(IUN,*)
             write(IUN,'(" Number of level-2 clusters for each level-3 cluster")')
             write(IUN,'(1x,13I6)') (IDNI(i,3), i=1,nlevel3)
             write(IUN,*)
        end if
             write(IUN,'(" Number of level-1 observations for each level-2 cluster")')
             write(IUN,'(1x,13I6)') (wIDNI(i,2), i=1,nlevel2)
             write(IUN,*)

        200  FORMAT(1x,A16,4F12.4)

             WRITE(IUN,*)
             WRITE(IUN,'(" Dependent variable")')
             WRITE(IUN,'("                         mean         min         max     std dev")') 
             WRITE(IUN,'(" ----------------------------------------------------------------")')
             WRITE(IUN,200) YLABEL,meany,miny,maxy,stdy
             WRITE(IUN,*)

             if (ncent==1) then
                WRITE(IUN,'(" ==> Standardization of covariates has been selected")') 
                WRITE(IUN,'(" ==> All covariates have mean=0 and std dev=1")') 
                WRITE(IUN,'(" ==> Means and std devs listed below are pre-standardization")') 
                WRITE(IUN,*)
             end if
             if (p>0) then
                WRITE(IUN,'(" Mean model covariates")')
             WRITE(IUN,'("                         mean         min         max     std dev")') 
             WRITE(IUN,'(" ----------------------------------------------------------------")')
                do i=1,p
                     meany=SUM(x(1:nobs,i))/DBLE(nobs)
                     miny=minval(x(1:nobs,i))
                     maxy=maxval(x(1:nobs,i))
                     tempR(1:nobs)=(x(1:nobs,i)-meany)**2
                     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
                   WRITE(IUN,200) BLAB(i),meany,miny,maxy,stdy
                   if(ncent == 1) x(1:nobs,i) = (x(1:nobs,i) - meany)/stdy
                end do
                WRITE(IUN,*)
             end if

             if (r>0) then
                WRITE(IUN,'(" BS variance model covariates")')
             WRITE(IUN,'("                         mean         min         max     std dev")') 
             WRITE(IUN,'(" ----------------------------------------------------------------")')
                do i=1,r
                     meany=SUM(u(1:nobs,i))/DBLE(nobs)
                     miny=minval(u(1:nobs,i))
                     maxy=maxval(u(1:nobs,i))
                     tempR(1:nobs)=(u(1:nobs,i)-meany)**2
                     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
                   WRITE(IUN,200) ALAB(i),meany,miny,maxy,stdy
                   if(ncent == 1) u(1:nobs,i) = (u(1:nobs,i) - meany)/stdy
                end do
                WRITE(IUN,*)
             end if

             if (s>0) then
                WRITE(IUN,'(" WS variance model covariates")')
             WRITE(IUN,'("                         mean         min         max     std dev")') 
             WRITE(IUN,'(" ----------------------------------------------------------------")')
                do i=1,s
                     meany=SUM(w(1:nobs,i))/DBLE(nobs)
                     miny=minval(w(1:nobs,i))
                     maxy=maxval(w(1:nobs,i))
                     tempR(1:nobs)=(w(1:nobs,i)-meany)**2
                     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
                   WRITE(IUN,200) tLAB(i),meany,miny,maxy,stdy
                   if(ncent == 1) w(1:nobs,i) = (w(1:nobs,i) - meany)/stdy
                end do
                WRITE(IUN,*)
             end if

             if (t>0) then
                if(waves == 1) then
                    WRITE(IUN,'(" Wave variance covariates")')
                else
                    WRITE(IUN,'(" Cluster variance covariates")')
                end if
             WRITE(IUN,'("                         mean         min         max     std dev")') 
             WRITE(IUN,'(" ----------------------------------------------------------------")')
                do i=1,t
                     meany=SUM(v(1:nobs,i))/DBLE(nobs)
                     miny=minval(v(1:nobs,i))
                     maxy=maxval(v(1:nobs,i))
                     tempR(1:nobs)=(v(1:nobs,i)-meany)**2
                     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
                   WRITE(IUN,200) gLAB(i),meany,miny,maxy,stdy
                   if(ncent == 1) v(1:nobs,i) = (v(1:nobs,i) - meany)/stdy
                end do
                WRITE(IUN,*)
             end if

             if (numrs>0) then
                WRITE(IUN,'(" Random Scale covariates")')
                WRITE(IUN,'("                         mean         min         max     std dev")') 
                WRITE(IUN,'(" ----------------------------------------------------------------")')
                do i=1,numrs
                     meany=SUM(rsvar(1:nobs,i))/DBLE(nobs)
                     miny=minval(rsvar(1:nobs,i))
                     maxy=maxval(rsvar(1:nobs,i))
                     tempR(1:nobs)=(rsvar(1:nobs,i)-meany)**2
                     stdy=sqrt(SUM(tempR)/DBLE(nobs-1))
                   WRITE(IUN,200) rsLABel(i),meany,miny,maxy,stdy
                   if(ncent == 1) rsvar(1:nobs,i) = (rsvar(1:nobs,i) - meany)/stdy
                end do
            end if
        CLOSE(IUN)
END SUBROUTINE PRINTDESC
    
SUBROUTINE estimate_level3()
    use mixregls32slope
    use procedures3
    implicit none
    INTEGER :: I,J,L,LL,l2,k,m,n,mynob,kk,IUN,CYCLES,NCYCLE,IFIN,ITER,npar_cycle,cholcol,counter,ii,myqdim2,&
               Q,NOB,RIDGEIT,IUNS,myqdim,totalqR0,totalqR1,mytotalq,nqC,qq,currenti,c,qo,qt,nq0,nqq,ncov_cycle,mytdim
    integer,allocatable :: myorder(:),myorder2(:),myorderinv(:),myorderinv2(:)
        REAL(KIND=bigreal) :: RIDGE,LOGLP,PSUM,LOGL,XB,ERRIJK,LPROB,LOGDIFF,MAXCORR,&
                    ORIDGE,PVAL,BIG,LOGBIG,MAXDER,psum2,zval,wt,mycoef,bestlogl,&
                    myz,tauhat,tauhatlow,tauhatup,VG,errijk1,qprob,us,iprob,log2pi,sigmae,qqprob,cprob,rtemp,small
    REAL(KIND=bigreal),ALLOCATABLE:: myider(:),myqder(:),mycder(:),mycder1(:,:),myder(:),myder2(:,:),qqprobs(:),tempmatBR(:,:), &
                               DERQ1(:,:),DERQ2(:,:),DZ1(:,:),COREC1(:,:),derqq1(:,:),derqq2(:,:),myqder2(:,:), &
                               theta1(:,:),dz1r(:,:),se(:),qprobs(:),thetav(:,:),WORK2(:),WORK3(:), theta1c(:,:),thetavc(:,:),&
                               temp(:,:),adjvar(:,:),pointsR0(:,:),weightsR0(:),pointsR1(:,:),weightsR1(:),corec(:),&
                               myider1(:,:),myider2(:,:),tempmat(:,:),mycder2(:,:),u2var(:,:),u2mean(:,:),&
                               work(:,:),asstar2(:,:),sigma(:),mycholspar(:),mycholspar2(:),chol2(:,:)

        ! parameters

     bestlogl  = -999999999999999.0
   log2pi = dlog(2*3.141592653589793238462643d0)   
    RTEMP = 1.0
    SMALL  = TINY(RTEMP)
    BIG    = HUGE(RTEMP)
    LOGBIG   = LOG(BIG)
    ndim = numloc + numrs
    ndim2 = (ndim+1)*ndim/2
    numloc2 = (numloc+1)*numloc/2
    IUN    = 16
    OPEN(UNIT=IUN,FILE="mixREGLS_3level2.OUT")
    IUNS    = 17
    OPEN(UNIT=IUNS,FILE="mixREGLS_3level_details_.ITS")
    open(unit=18, file="MixRegls_3level_.its")
    totalqR1 = NQ**(numloc+numrs)
    totalqR0 = nq**numloc
    allocate(weightsR1(totalqR1))
    allocate(pointsR1(totalqR1,numloc+numrs))
    allocate(weightsR0(totalqR0))
    allocate(pointsR0(totalqR0,numloc))
    allocate(weights1(nq))
    allocate(points1(nq,1))
    allocate(myweightsC(nq))
    allocate(mypointsC(nq,1))
    allocate(gamma(t))
    call getquad(numloc, nq, totalqR0, pointsR0, weightsR0)
    call getquad(numloc+numrs, nq, totalqR1, pointsR1, weightsR1)
    call getquad(1, nq, nq, points1, weights1)
    ! number of quadrature points, quadrature nodes & weights
    myqdim = ndim
    myqdim2 = myqdim*(myqdim+1)/2
    mytdim = 0
        !Establishing arrays at maximum size needed
        ! NS = number of additional var cov parameters due to random SCALE
        NS = ndim2-numloc2
        if(ncov .eq. 2) ns = 3
!        if(ncov .eq. 0) ns = numrs2
        npar = p+s+ns+RR*mls+nalpha+t
        NPAR2 = NPAR*(NPAR+1)/2
    allocate(myder(npar))
    allocate(myqder(npar))
    allocate(myder2(npar,npar))
    allocate(myqder2(npar,npar))
    allocate(corec(npar))
    allocate(corec1(npar,1))
    allocate(myider1(npar,1))
    allocate(myider(npar))
    allocate(myider2(npar,npar))
    allocate(mycder1(npar,1))
    allocate(mycder2(npar,npar))
    allocate(mycder(npar))
    allocate(tempmat(npar,npar))
    allocate(tempmatBR(npar,npar))
    ALLOCATE (SE(NPAR))
    ALLOCATE (THETA1(ndim,1))
    ALLOCATE (thetav(ndim,ndim))
    ALLOCATE (THETA1c(1,1))
    ALLOCATE (thetavc(1,1))
    ALLOCATE (THETAsc(ntclust,1))
    ALLOCATE (thetavsc(ntclust,1,1))
    cholamt=max(ndim2,numloc2+ns)
    allocate(sstar(cholamt,cholamt))
    allocate(work(npar,npar))
    allocate(adjVar(npar,npar))
    allocate(asstar2(npar,npar))
    allocate(sigma(cholamt))
    allocate(mycholspar(cholamt))
    allocate(mycholspar2(cholamt))
    allocate(qprobs(totalqR1))
    allocate(u2mean(ndim,1))
    allocate(u2var(ndim,ndim))
    allocate(chol2(ndim,ndim))
    allocate(qqprobs(totalqR1))
    allocate(myorder(ndim2))
    allocate(myorder2(ndim2))
    allocate(myorderinv(ndim2))
    allocate(myorderinv2(ndim2))

        ALLOCATE (DERQ1(NPAR,1))
        ALLOCATE (DERQ2(NPAR,npar))
        ALLOCATE (DERQq1(NPAR,1))
        ALLOCATE (DERQq2(NPAR,npar))
        ALLOCATE (DZ1(NPAR,1))
        ALLOCATE (DZ1r(NPAR,1))
        ALLOCATE (temp(NPAR,npar))
        ALLOCATE (WORK2(ndim))
        ALLOCATE (WORK3(ndim2))
        allocate(myweights(totalqR1))
        allocate(mypoints(totalqR1,numloc+numrs))
        allocate(myweights0(totalqR1))
        allocate(mypoints0(totalqR1,numloc+numrs))
!        allocate(varSQ(npar,npar))

        ! start cycles
        ! cycles = 1: random intercept model with BS variance terms
        ! cycles = 2: add in scale (WS) variance terms
        ! cycles = 3: add in random scale 
        ! cycles = 4: use NS = R+1
        
        corec = 0        
!ncov = 0
        t_cycle = 0
        s_cycle = 1
        ns_cycle = 0
        ncov_cycle = 0
        ncycle = 5
    if(ncov .eq. 0) ncycle = 4
    if(nors .eq. 1) ncycle = 3
    CYCLELOOP:do cycles=1,ncycle
        if (cycles==1) then
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("------------------------------")')
            WRITE(IUN,'("Model without Scale Parameters")')
            WRITE(IUN,'("------------------------------")')
            WRITE(*,*)
            WRITE(*,*) "------------------------------"
            WRITE(*,*) "Model without Scale Parameters"
            WRITE(*,*) "------------------------------"
            WRITE(IUNS,*)
            WRITE(IUNS,*) "------------------------------"
            WRITE(IUNS,*) "Model without Scale Parameters"
            WRITE(IUNS,*) "------------------------------"
            myqdim = numloc
            myqdim2 = (myqdim+1)*myqdim/2
            mytotalq = totalqR0
            mypoints0(1:mytotalq,1:myqdim) = pointsR0
            myweights0(1:mytotalq) = weightsR0
            mypointsC = 0
            myweightsC = 1
            nqC = 1
            gamma = 0
        else if (CYCLES==2) THEN
            if(no3 .eq. 1) cycle
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("---------------------------")')
            WRITE(IUN,'("3 level Model without Scale Parameters")')
            WRITE(IUN,'("---------------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("---------------------------")')
            WRITE(IUNS,'("3 level Model without Scale Parameters")')
            WRITE(IUNS,'("---------------------------")')
            WRITE(*,*)
            WRITE(*,*) "---------------------------"
            WRITE(*,*) "3 level Model without Scale Parameters"
            WRITE(*,*) "---------------------------"
            t_cycle = t
            nqC = nq
            mypointsC = points1
            myweightsC = weights1
            gamma = 0
            gamma(1) = -2
            mytdim = 1
        else if (CYCLES==3) THEN
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("---------------------------")')
            WRITE(IUN,'("Model WITH Scale Parameters")')
            WRITE(IUN,'("---------------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("---------------------------")')
            WRITE(IUNS,'("Model WITH Scale Parameters")')
            WRITE(IUNS,'("---------------------------")')
            WRITE(*,*)
            WRITE(*,*) "---------------------------"
            WRITE(*,*) "Model WITH Scale Parameters"
            WRITE(*,*) "---------------------------"
            s_cycle = s
        else if (CYCLES==4) THEN
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("-----------------------")')
            WRITE(IUN,'("Model WITH RANDOM Scale")')
            WRITE(IUN,'("-----------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("-----------------------")')
            WRITE(IUNS,'("Model WITH RANDOM Scale")')
            WRITE(IUNS,'("-----------------------")')
            WRITE(*,*)
            WRITE(*,*) "-----------------------"
            WRITE(*,*) "Model WITH RANDOM Scale"
            WRITE(*,*) "-----------------------"
            NS_cycle = numrs2 ! number of additional var cov parameters due to random SCALE
            myqdim = numloc+numrs
            myqdim2 = (myqdim+1)*myqdim/2
            mytotalq = totalqR1
            mypoints0 = pointsR1
            myweights0 = weightsR1
            spar(1:ns_cycle) = 0
            spar(1) = .2
            spar(numrs2) = .2
        else if (CYCLES==5) THEN
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("-----------------------")')
            WRITE(IUN,'("Model WITH correlated RANDOM Scale")')
            WRITE(IUN,'("-----------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("-----------------------")')
            WRITE(IUNS,'("Model WITH correlated RANDOM Scale")')
            WRITE(IUNS,'("-----------------------")')
            WRITE(*,*)
            WRITE(*,*) "-----------------------"
            WRITE(*,*) "Model WITH correlated RANDOM Scale"
            WRITE(*,*) "-----------------------"
            if(numrs > 1) then
                spar((ns-numrs+1):ns) = spar(2:numrs2)
                spar(2:numrs2) = 0
            end if
            NS_cycle = ns
            spar(numloc+1) = spar(1)
            spar(1:numloc) = 0
            ncov_cycle = ncov
        end if
        NPAR_cycle = P+RR*mls+nalpha+S_cycle+t_cycle+NS_cycle
        nmeaneff = p+rr*mls+nalpha+t_cycle
!write(*,*) npar_cycle, numrs, nmeaneff, ns_cycle, ns, myqdim2, numloc2, ntclust, nsubj
!write(*,*) alpha, beta, gamma        !
    ! start iterations
    !

         ! ifin = 1 for iterations using NR (or BHHH depending on the inversion of DER2)
         !      = 2 for final iteration
         !

         ridge  = ridgein
         RIDGEIT= 0

         ! set the ridge up for the models with scale parameters
         if (CYCLES<=1) then
            ridge = ridgein+.05D0
         END IF
         IF (CYCLES==3 .or. cycles==2) THEN
            ridge = RIDGEIN+.2D0
         END IF
         if(cycles >= 4) ridge = ridgein + .4
         if(ridge < 0) ridge = ridgein

         loglp  = -999999999999999.0

        ITER=1
    ! START WITH NEWTON RAPHSON (MIXREG for starting values)
        IFIN=1
        IFINLOOP:DO WHILE (ifin < 3)

             ! set ifin=2 if on the last iteration
            if (ifin == 2) then
                 ifin = 3
             end if

             ! put the ridge back to its initial value after the first 5 & 10 iterations
!            IF (CYCLES>=3 .AND. ITER==11) THEN
!                ridge = ridge - .2
!             else 
             if(ridge < 0) ridge = 0 !To get rid of -0 values
             
        !
        ! calculate the derivatives and information matrix
        !
!if(cycles > 2 .and. iter .eq. 1) write(203,'(i6,27g15.5)') cycles,thetasc(1:10,1), thetavsc(1:10,1,1)

            LOGL= 0.0D0
            myDER(1:npar_cycle)=0.0D0
            myder2(1:npar_cycle,1:npar_cycle) = 0.0D0
            nob = 0
            currenti = 0
            mynob = 0
            if(waves == 1) then
                nq0 = nqc
                nqq = mytotalq
            else
                nqq = nqc
                nq0 = mytotalq
            end if                            
            
            cloop: do c=1,nlevel3 !go over level-3 clusters
                if(waves == 0) then
                    call adaptive(mytdim,nqc,c,.FALSE.,t_cycle>0 .and. aquad3 .ne. 0 .and. (iter >= 10 .or. cycles > 2))
                else
                    call adaptive(myqdim,mytotalq,c,.TRUE.,myqdim>0 .and. aquad .ne. 0 .and. (iter >= 10 .or. cycles .ne. 4))
                end if
                mycder1(1:npar_cycle,1) = 0
                mycder2(1:npar_cycle,1:npar_cycle) = 0
                cprob=0
                qqloop: do qq=1,nqq
                    DERQq1(1:npar_cycle,1)  = 0.0D0
                    DERQq2(1:npar_cycle,1:npar_cycle)  = 0.0D0
                    psum2=0
                    ILOOP:DO I=1,idni(c,3)  ! go over level-2 clusters
                        if(waves == 1) then
                            call adaptive(mytdim,nqc,currenti+i,.FALSE.,t_cycle>0 .and. aquad3/=0 .and. &
                            (iter>5 .or. cycles .eq. 3 .or. cycles .eq. 5))
!                            if(aquad /= 0 .and. cycles > 3 .and. iter == 1) write(179,'(3i4,27g13.5)') c,currenti+i,qq,&
!                                thetasc(currenti+i,1),thetavsc(currenti+i,1,1),mypointsC(6,1),myweightsC(6)
                        else
                            call adaptive(myqdim,mytotalq,currenti+i,.TRUE.,myqdim>0 .and. aquad/=0 .and. (iter>=10 .or. cycles/=4))
                        end if
                        iprob = 0
                        myider1(1:npar_cycle,1) = 0
                        myider2(1:npar_cycle,1:npar_cycle) = 0

                        QLOOP:DO Q=1, nq0 ! go over quadrature points
                            DERQ1(1:npar_cycle,1)  = 0.0D0
                            DERQ2(1:npar_cycle,1:npar_cycle) = 0.0D0
                            PSUM     = 0.0D0
                            if(waves == 1) then
                                qt = q
                                qo = qq
                            else
                                qt = qq
                                qo = q
                            end if                            
                            JLOOP: do j=1,widni(currenti+i,2)
                                nob = mynob + j + sum(widni(currenti+1:currenti+i-1,2))
                                DZ1(1:npar_cycle,1)    = 0.0D0

                                XB = DOT_PRODUCT(BETA,X(NOB,:))        ! X BETA for the current LEVEL-1 obs
                                WT = DOT_PRODUCT(TAU(1:S_cycle),W(NOB,1:S_cycle))  ! W TAU for the current LEVEL-1 obs
                                vg = 0
!if(cycles .eq. 4 .and. c .eq. 1 .and. i .eq. 1 .and. q .eq. 1 .and. iter .eq. 1) &
!    write(203,*) c, qq, i, q, j, wt
                                if(t_cycle > 0) VG = mypointsC(qt,1)*exp(.5*DOT_PRODUCT(gamma(1:t_cycle),v(NOB,1:t_cycle)) ) ! 3rd level term variance
                                if (ns_cycle > 0) then
                                    n=0
                                    do k=1, numrs
                                        if(ns_cycle .eq. numrs2) then
                                            do m=1, k
                                                n = n + 1
                                                wt = wt + rsvar(nob,k) * spar(n) * mypoints(qo, numloc+m)
                                            end do
                                        else
                                            do m=1, numloc+k
                                                n = n + 1
                                                wt = wt + rsvar(nob,k) * spar(n) * mypoints(qo, m)
    !write(iuns,*) mychol(t),u(nob,k),us,mypoints(q,m)
                                            end do
                                        end if
                                    end do
                                    if(ncov_cycle .eq. 2) wt = wt + spar(ns_cycle) * mypoints(qo, 1)**2
                                end if    
!Covariance between location and scale effects not currently included
                        !     Random effect term (linear predictor?)
                                Us = 0  ! Either Ualpha or UsigmaTheta1 depending on model
                                if(rr*mls > 0) then
                                    n=0
                                    do k=1, numloc
                                        do m=1, k
                                            n = n + 1
                                            Us = Us + U(nob,k) * mychol(n) * mypoints(qo, m)
!write(iuns,*) mychol(t),u(nob,k),us,mypoints(q,m)
                                        end do
                                    end do
                                else if(nalpha > 0) then
!write(iuns,*) nalpha,alpha(1:nalpha),u(nob,1:nalpha),mypoints(q,1),dot_product(alpha(1:nalpha),U(nob,1:nalpha))
                                    if(dot_product(alpha(1:nalpha),U(nob,1:nalpha)) < logbig) then
                                        Us = mypoints(qo,1) * exp(.5*dot_product(alpha(1:nalpha),U(nob,1:nalpha)))
                                    else
                                        Us = big
                                    end if
                                end if
                                IF (wt .GE. LOGBIG) THEN
                                    sigmae = BIG
                                ELSE
                                    sigmae = EXP(.5*wt)
                                END IF
                                IF (sigmae .LE. SMALL) sigmae = SMALL   
                                errijk = y(nob) - (xb + us + vg)
                                errijk1 = errijk/sigmae
                                lprob = -.5*log2pi - log(sigmae) - .5*errijk1**2
                                PSUM  = PSUM + LPROB
!write(69,'(7i6,27g15.5)') c,currenti+i,j,mynob,nob,qt,qo,y(nob),xb,us,mypoints(qo,1),vg,errijk,sigmae,errijk1,lprob,psum
!write(iuns,'(27g25.5)') us,mypoints(qo,1),dot_product(alpha(1:nalpha),U(nob,1:nalpha)),alpha(1:nalpha),U(nob,1:nalpha)
                                ! GET FIRST DERIVATIVES
                                DZ1(1:P,1) = ERRIJk1*X(NOB,1:p)/sigmae                    ! beta
                                if(mls .eq. 1) then
                                    n=0
                                    do k=1, numloc
                                        do m=1, k
                                            n = n + 1
                                            dz1(p+n,1) = U(nob,k) * mypoints(qo, m)*errijk1/sigmae    !T
                                        end do 
                                    end do
                                else
                                    DZ1(P+1:P+nalpha,1) = ERRIJk1/sigmae*Us*U(NOB,1:nalpha)/2 ! alpha
                                end if
                                DZ1((nmeaneff+1):(nmeaneff+S_cycle),1) = (-1 +ERRIJk1**2) * W(NOB,1:S_cycle)/2    ! tau
                                if(t_cycle > 0) DZ1(nmeaneff-t_cycle+1:nmeaneff,1) = ERRIJk1/sigmae*vg*v(NOB,1:t_cycle)/2 ! 3 level
                                if (ns_cycle > 0) then
                                    n=0
                                    do k=1, numrs
                                        if(ns_cycle .eq. numrs2) then
                                            do m=1, k
                                                n = n + 1
                                                dz1(nmeaneff+s_cycle+n,1)=&
                                                    (-1+ERRIJk1**2)*rsvar(nob,k)*mypoints(qo, numloc+m)/2
                                            end do
                                        else
                                            do m=1, numloc+k
                                                n = n + 1
                                                dz1(nmeaneff+s_cycle+n,1) = (-1+ERRIJk1**2)*rsvar(nob,k) * mypoints(qo, m)/2
                                            end do
                                        end if
                                    end do
                                    if(ncov_cycle == 2) dz1(npar_cycle,1) = (-1+ERRIJk1**2)*mypoints(qo, 1)**2/2
                                end if
                                DERq1(1:npar_cycle,1) = DERq1(1:npar_cycle,1) + DZ1(1:npar_cycle,1)

!write(iuns,*) psum,(dz1(k,1),k=1,npar_cycle)
                                dz1r(1:npar_cycle,1) = dz1(1:npar_cycle,1)/errijk1
                                DZ1r((nmeaneff+1):(nmeaneff+S_cycle),1) = (ERRIJk1)*W(NOB,1:S_cycle)!sqrt(2.)    ! tau
                                if (ns_cycle > 0) then
                                    n=0
                                    do k=1, numrs
                                        if(ns_cycle .eq. numrs2) then
                                            do m=1, k
                                                n = n + 1
                                                dz1r(nmeaneff+s_cycle+n,1) = ERRIJk1*rsvar(nob,k) * mypoints(qo, numloc+m)!/sqrt(2.)
                                            end do
                                        else
                                            do m=1, numloc+k
                                                n = n + 1
                                                dz1r(nmeaneff+s_cycle+n,1) = ERRIJk1*rsvar(nob,k) * mypoints(qo, m)!/sqrt(2.)
                                            end do
                                        end if
                                    end do
                                    if(ncov_cycle == 2) dz1r(npar_cycle,1) = ERRIJk1*mypoints(qo, 1)**2
                                end if
                                tempmat(1:npar_cycle,1:npar_cycle) = matmul(dz1r(1:npar_cycle,:), transpose(dz1r(1:npar_cycle,:)))
                                tempmat(nmeaneff+1:npar_cycle,nmeaneff+1:npar_cycle) = &
                                                                       tempmat(nmeaneff+1:npar_cycle, nmeaneff+1:npar_cycle)/2
                                if(nalpha > 0) then
                                    tempmat(p+1:p+nalpha,p+1:p+nalpha) = tempmat(p+1:p+nalpha,p+1:p+nalpha) &
                                                                    - matmul(dz1(p+1:p+nalpha,:),u(nob:nob,1:nalpha))/2
                                end if
                                if(t_cycle > 0) then
                                    n = nmeaneff-t_cycle+1
                                    tempmat(n:nmeaneff,n:nmeaneff) = tempmat(n:nmeaneff,n:nmeaneff) &
                                                            - matmul(dz1(n:nmeaneff,:),v(nob:nob,1:t_cycle))/2
                                    
                                end if
                                derq2(1:npar_cycle,1:npar_cycle) = derq2(1:npar_cycle,1:npar_cycle) - &
                                                                    tempmat(1:npar_cycle,1:npar_cycle)
                            end do jloop
                            if(waves == 1) then
                                qprob = exp(psum)*myweightsC(q)
                            else
                                qprob = exp(psum)*myweights(q)
                            end if
                            qprobs(q) = qprob
                            iprob = iprob + qprob
                            myider1(1:npar_cycle,1) = myider1(1:npar_cycle,1) + derq1(1:npar_cycle,1)*qprob
                            myider2(1:npar_cycle,1:npar_cycle) = myider2(1:npar_cycle,1:npar_cycle) + &
                                                            derq2(1:npar_cycle,1:npar_cycle)*qprob + &
                                                    matmul(derq1(1:npar_cycle,:), transpose(derq1(1:npar_cycle,:)))*qprob
                        END DO qLOOP
                        if(iprob < small) iprob = small
                        if(isnan(iprob)) then 
                            iprob = small
write(iuns,*) 'NaN detected in iprob',c,i, alpha, beta, xb, us
write(109,*) 'NaN detected in iprob',c,i, alpha, beta, xb, us
                        end if                            

                        myider1(1:npar_cycle,1) = myider1(1:npar_cycle,1)/iprob
                        derqq1(1:npar_cycle,1) = derqq1(1:npar_cycle,1) + myider1(1:npar_cycle,1)
                        psum2 = psum2 + log(iprob)
                        tempmat(1:npar_cycle,1:npar_cycle) = matmul(myider1(1:npar_cycle,:),transpose(myider1(1:npar_cycle,:))) &
                                                                - myider2(1:npar_cycle,1:npar_cycle)/iprob 
                        derqq2(1:npar_cycle,1:npar_cycle) = derqq2(1:npar_cycle,1:npar_cycle) - &
                                                                tempmat(1:npar_cycle,1:npar_cycle)
                        if(waves == 0 .and. myqdim > 0) then
                            call calcThetas(myqdim,mytotalq,currenti+i,qprobs,iprob,.TRUE.)
                        end if
                        if(waves == 1 .and. t_cycle > 0) then
                            call calcThetas(mytdim,nqc,currenti+i,qprobs,iprob,.FALSE.)
                        end if
                   END DO ILOOP
                    if(waves == 1) then
                        qqprob = exp(psum2)*myweights(qq)
                    else
                        qqprob = exp(psum2)*myweightsC(qq)
                    end if
                    if(qqprob < small) qqprob = small
                    qqprobs(qq) = qqprob
                    mycder1(1:npar_cycle,1) = mycder1(1:npar_cycle,1) + derqq1(1:npar_cycle,1)*qqprob
                    mycder2(1:npar_cycle,1:npar_cycle) = mycder2(1:npar_cycle,1:npar_cycle) + &
                                                    derqq2(1:npar_cycle,1:npar_cycle)*qqprob + &
                                                    matmul(derqq1(1:npar_cycle,:),transpose(derqq1(1:npar_cycle,:)))*qqprob
                    cprob = cprob + qqprob
                end do qqloop
                if(cprob <= SMALL) cprob=SMALL
                logl = logl + log(cprob)
                mycder1(1:npar_cycle,1) = mycder1(1:npar_cycle,1)/cprob
                myder(1:npar_cycle) = myder(1:npar_cycle) + mycder1(1:npar_cycle,1)
                tempmatBR(1:npar_cycle,1:npar_cycle) = matmul(mycder1(1:npar_cycle,:),transpose(mycder1(1:npar_cycle,:))) &
                                                        - mycder2(1:npar_cycle,1:npar_cycle)/cprob
                myder2(1:npar_cycle,1:npar_cycle) = myder2(1:npar_cycle,1:npar_cycle) + tempmatBR(1:npar_cycle,1:npar_cycle)
                if(waves == 1 .and. myqdim > 0) then
                    call calcThetas(myqdim,mytotalq,c,qqprobs,cprob,.TRUE.)
                end if
                if(waves == 0 .and. t_cycle > 0) then
                    call calcThetas(mytdim,nqc,c,qqprobs,cprob,.FALSE.)
                end if

                mynob = mynob + idni(c,2)
                currenti = currenti + idni(c,3)
            end do cloop

                 LOGDIFF = LOGL-LOGLP
                 LOGLP   = LOGL

                 ! determine if an NR iteration is bad and increase the ridge
                 ! take the ridge off after 10 good iterations
             IF (LOGDIFF/LOGLP > .005 .AND. ITER < MAXIT) THEN
                RIDGEIT = 0
                RIDGE = RIDGE + .1D0
                WRITE(IUNS,'("==> BAD NR ITERATION ",I5," with NEW ridge = ",F8.4,/)') ITER,RIDGE
                corec(1:npar_cycle) = -.5 * corec(1:npar_cycle)
                GO TO 99
             END IF
             IF (LOGDIFF/LOGLP <= .000001 .AND. RIDGEIT < 10) THEN
                RIDGEIT = RIDGEIT+1
             ELSE IF (LOGDIFF/LOGLP <= .000001 .AND. RIDGEIT >= 10 .and. ifin==1) then
                ridge = ridge - .1
                ridgeit=0
             END IF
             if(ridge < 0) ridge = 0
                 if(maxder < 2 .and. iter > 10) ridge = 0

 write(IUNS,*)"2nd Derivatives without ridge"
                do k=1,npar_cycle
                    write(IUNS,'(25g15.5)') (myder2(k,i), i=1,k)
                end do
                if(ifin < 2) then
                 ! ridge adjustment - diagonal elements only
                do k=1,npar_cycle
                    myder2(k,k) = abs(myder2(k,k))*(1 + ridge)
                end do
            end if
        
            temp(1:npar_cycle,1:npar_cycle) = myder2(1:npar_cycle,1:npar_cycle)
            call inverse(temp(1:npar_cycle,1:npar_cycle), temp(1:npar_cycle,1:npar_cycle), npar_cycle)
            corec1(1:npar_cycle,1) = myder(1:npar_cycle)
            corec1(1:npar_cycle,1) = matmul(temp(1:npar_cycle,1:npar_cycle), corec1(1:npar_cycle,1))
            if(iter<=5 .and. cycles > 1) corec1(1:npar_cycle,1) = corec1(1:npar_cycle,1)*.5
            corec(1:npar_cycle) = corec1(1:npar_cycle,1)


            write(IUNS,*)"Corrections"
            write(IUNS,'(25g15.5)') (corec1(k,1), k=1,npar_cycle)
            write(IUNS,*)"Derivatives"
            write(IUNS,'(25g15.5)') (myder(k), k=1,npar_cycle)
            write(IUNS,*)"Beta"
            write(IUNS,'(25f15.3)') (beta(k), k=1,p)
            if(RR*mls > 0) then
                write(IUNS,*)"Chol"
                write(IUNS,'(25f15.3)') (mychol(k),k=1,rr)
            end if
            if(nalpha > 0) then
                write(IUNS,*)"Alpha"
                write(IUNS,'(25f15.3)') (alpha(k),k=1,nalpha)
            end if
            if(t_cycle > 0) then
                write(IUNS,*)"Gamma"
                write(IUNS,'(25f15.3)') (gamma(k),k=1,t_cycle)
            end if
            write(IUNS,*)"Tau"
            write(IUNS,'(25f15.3)') (tau(k),k=1,s_cycle)
            if(ns_cycle > 0) then
                write(IUNS,*)"Spar"
                write(IUNS,'(25f15.3)') (spar(k),k=1,ns_cycle)
            end if
            MAXDER=MAXVAL(ABS(myDER(1:npar_cycle)))
            MAXCORR=MAXVAL(ABS(COREC(1:npar_cycle)))
            WRITE(*,*) iter,'  maximum correction and derivative and ridge'
            WRITE(*,'(25g15.5)') maxcorr,MAXDER,ridge
            WRITE(IUNS,*) iter,'  maximum correction and derivative and ridge'
            WRITE(IUNS,'(25g15.5)') MAXCORR,MAXDER,ridge
        
             ! done with NR and onto last iteration
             IF (IFIN==1 .AND. (MAXCORR <= CONV .OR. ITER >= MAXIT)) THEN
                 IFIN=2
                 ORIDGE=RIDGE
                 RIDGE=0.0D0
             END IF
            do k=1,npar_cycle
                if(corec(k) > 1) corec(k) = .5
                if(corec(k) < -1) corec(k) = -.5
            end do
            if(iter<=10) corec(1:npar_cycle) = corec(1:npar_cycle)/2
             ! UPDATE PARAMETERS

         99  WRITE(*,'("   -2 Log-Likelihood = ",F14.5)') -2*LOGL
             WRITE(IUNS,'("   -2 Log-Likelihood = ",F14.5)') -2*LOGL
!            write(IUNS,*)"Corrections"
!            write(IUNS,'(25f11.3)') (corec(k), k=1,npar_cycle)

            beta = beta + corec(1:p)
            if (s_cycle > 0) TAU(1:s_cycle)   = TAU(1:s_cycle) + COREC(nmeaneff+1:nmeaneff+S_cycle)
            if (t_cycle > 0) gamma(1:t_cycle)   = gamma(1:t_cycle) + COREC(nmeaneff-t_cycle+1:nmeaneff)
            if(RR*mls > 0 .and. iter > 14) then
                mychol = mychol + COREC(P+1:P+RR)/2
                kk = 1
                    do k=1, r
                        do LL=1, k
                            if(k==LL .and. mychol(kk) < 0) mychol(kk) = -mychol(kk)
                            kk = kk + 1
                        end do
                    end do
            else if(nalpha > 0) then
                alpha = alpha + COREC(P+1:P+nalpha)
            end if
            if(ns_cycle > 0 .and. (iter > 6 .or. (iter > 1 .and. cycles >= 5))) then
                do k=1,ns_cycle
                        spar(k) = spar(k) + corec(npar_cycle-ns_cycle+k)/2
                end do
                if(spar(ns_cycle) < 0 .and. ncov_cycle < 2) spar(ns_cycle) = abs(spar(ns_cycle))
                if(spar(1) < 0 .and. ncov_cycle == 0) spar(1) = abs(spar(1))
                if(spar(2) < 0 .and. ncov_cycle == 2) spar(2) = abs(spar(2))
                if(numrs > 1) then
                    if(ns_cycle .eq. numrs2) then
                        spar(1) = abs(spar(1))
                    else
                        spar(numloc+1) = abs(spar(numloc+1))
                    end if
                end if
            end if

                 ITER = ITER+1
            END DO IFINLOOP
            if(logl < bestlogl) then
                WRITE(IUN,*) "------------------------------------------------------------------------"
                WRITE(IUN,*) "WARNING: THIS MODEL IS LIKELY INACCURATE SINCE LOG LIKELIHOOD INCREASED!"
                WRITE(IUN,*) "------------------------------------------------------------------------"
            else
                bestlogl = logl
            end if
            if(mls==0 .and. rold+rv==0 .and. chol<2) then
                mychol(1) = exp(alpha(1)/2)
                do k=1,npar_cycle
                    temp(p+1,k) = temp(p+1,k)*mychol(1)*.5
                    temp(k,p+1) = temp(k,p+1)*mychol(1)*.5
                end do
            end if
            adjvar = temp
            cholamt = rr
            cholcol = numloc
write(111,*) cholamt, cholcol
write(*,*) cholamt, cholcol
            mycholspar(1:rr) = mychol(1:rr)
            sigma(1:rr) = mychol(1:rr)
            do j=1,myqdim2
                myorder(j) = j
                myorderinv(j) = j
            end do
            myorder2 = myorder
            if(cycles == 5) then 
                mycholspar(rr+1:rr+ns) = spar(1:ns)
                sigma(rr+1:rr+ns) = spar(1:ns)
                if(chol .ne. 1 .and. chol .ne. 2) then
                    cholamt = rr+ns
                    cholcol = myqdim
                    counter = 1
                    do j=0,ndim-1
                        do i=1,ndim-j
                            myorder(counter) = (j+i)*(j+i-1)/2 + j + 1
                            myorderinv(myorder(counter)) = counter
                            counter = counter + 1
                        end do
                    end do
                end if
            end if
write(111,*) cholamt, cholcol
write(*,*) cholamt, cholcol
            do j=1,cholamt
                myorder2(j) = myorder(j)+p
                if(myorder(j)>rr) myorder2(j) = myorder2(j)+s+t
                myorderinv2(j) = myorderinv(j)+p
                if(myorder(j)>rr) myorderinv2(j) = myorderinv2(j)+s+nalpha-1+t
            end do
write(111,*) "Cholesky"
write(111,'(20f6.2)') (mycholspar(j),j=1,cholamt)
            do i=1,cholamt
                mycholspar2(i) = mycholspar(myorder(i))
            end do
write(111,*) "Ordering"
write(111,'(20i3)') (myorder(j),j=1,cholamt)
write(111,'(20i3)') (myorder2(j),j=1,cholamt)
write(111,'(20i3)') (myorderinv(j),j=1,cholamt)
write(111,'(20i3)') (myorderinv2(j),j=1,cholamt)

write(111,*) "Rearranged Cholesky"
write(111,'(20f6.2)') (mycholspar2(j),j=1,cholamt)
            do i=1,cholamt
                mycholspar2(i) = mycholspar(myorder(i))
            end do
            if(chol .ne. 2) then
write(111,*) cholamt, cholcol
write(*,*) cholamt, cholcol
write(111,*) "Matrix to transform cholesky back"
                call getSStarRev2(mycholspar2(1:cholamt),cholcol,cholamt,sstar(1:cholamt,1:cholamt))
                do i=1,cholamt
    write(111,'(20f6.2)') (sstar(i,j),j=1,cholamt)
                    sigma(i) = dot_product(mycholspar2(1:cholamt),sstar(i,1:cholamt))
                end do
write(111,*) "Sigma"
write(111,'(20f6.2)') (sigma(j),j=1,cholamt)
write(111,*) "Rearranged Sigma"
write(111,'(20f6.2)') (sigma(myorderinv(j)),j=1,cholamt)
                asstar2 = 0
                do i=1,npar_cycle
                    asstar2(i,i) = 1
                end do
                ii=1
                do i=1,cholamt
                    do j=1,cholamt
                        asstar2(myorder2(i),myorder2(j)) = 2*sstar(i,j)
                    end do
                end do
write(111,*) "Matrix to transform var/covar"
do i=1,npar_cycle
    write(111,'(20f6.2)') (asstar2(i,j),j=1,i)
end do
write(111,*) "Original var/covar values"
do i=1,npar_cycle
    write(111,'(20F12.4)') (temp(i,j), j=1,i)
end do
                work(1:npar_cycle,1:npar_cycle) = matmul(asstar2(1:npar_cycle,1:npar_cycle), &
                                                            temp(1:npar_cycle,1:npar_cycle))        
                adjVar(1:npar_cycle,1:npar_cycle) = matmul(work(1:npar_cycle,1:npar_cycle),&
                                                transpose(asstar2(1:npar_cycle,1:npar_cycle)))
write(111,*) "Transformed var/covar values"
do i=1,npar_cycle
    write(111,'(20F12.4)') (adjvar(i,j), j=1,i)
end do
            else
                sigma(1:rr) = mycholspar(1:rr)
                adjvar = temp
            end if

            DO k=1,npar_cycle
                se(k) = sqrt(abs(adjvar(k,k)))
            END DO
            
!            if((cycles .ne. 3) .or. (cycles == 3 .and. ncov0 == 0)) then
             ! WRITE RESULTS
           WRITE(IUN,562)ITER-1,ORIDGE,LOGL,LOGL-NPAR, &
           LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)),0-2*LOGL,0-2*(LOGL-NPAR), &
           0-2*(LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)))
           562 FORMAT(1X,'Total  Iterations =',I4,/, &
                      1X,'Final Ridge value =',F4.1,//, &
                 1X,'Log Likelihood                 = ',F12.3,/, &
                 1X,"Akaike's Information Criterion = ",F12.3,/, &
                 1X,"Schwarz's Bayesian Criterion   = ",F12.3,//,&
                 1X,"==> multiplied by -2             ",      /  &
                 1X,'Log Likelihood                 = ',F12.3,/, &
                 1X,"Akaike's Information Criterion = ",F12.3,/, &
                 1X,"Schwarz's Bayesian Criterion   = ",F12.3,/)
            WRITE(IUN,57)
57 FORMAT(/,'Variable',16x,'  Estimate',4X,'AsymStdErr',4x, &
      '   z-value',4X,'   p-value',/,'----------------',8x,  &
      '----------',4X,'----------',4X,'----------',4X,'----------')
        
             PVAL=0.0D0
             ZVAL=0.0D0
        
             IF (NCENT==1 .AND. P>1) THEN
                 WRITE(IUN,'("STANDARDIZED BETA (regression coefficients)")')
             ELSE
                 WRITE(IUN,'("BETA (regression coefficients)")')
             END IF
             DO L=1,P
                ZVAL = BETA(L)/SE(L)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)BLAB(L),BETA(L),SE(L),ZVAL,PVAL
             END DO

            if(nalpha>0 .and. chol>1) then
                 IF (NCENT==1) THEN
                     WRITE(IUN,'("STANDARDIZED ALPHA (BS variance parameters: log-linear model)")')
                 ELSE
                     WRITE(IUN,'("ALPHA (BS variance parameters: log-linear model)")')
                 END IF
                 DO L=1,nalpha
                    L2 = P+L
                    ZVAL = ALPHA(L)/SE(L2)
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,804)ALAB(L),ALPHA(L),SE(L2),ZVAL,PVAL
                 END DO
            else
                if(chol .ne. 2) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM LOCATION EFFECT Variance Matrix")')
                else
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM LOCATION EFFECT Cholesky Matrix")')
                end if
                k = 1
                do i=1,r
                    do j=1,i
                        mycoef = sigma(myorderinv(k))
                        ZVAL = mycoef/SE(k+p)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        if(i .eq. j) then
                            WRITE(IUN,804)alab(i),mycoef,SE(k+p),ZVAL,PVAL
                        else if(chol .ne. 2) then
                            WRITE(IUN,804)'Covariance      ',mycoef,SE(k+p),ZVAL,PVAL
                        else
                            WRITE(IUN,804)'Cholesky        ',mycoef,SE(k+p),ZVAL,PVAL
                        end if
                        k = k+1
                    end do
                END DO
            end if
             IF (NCENT==1 .AND. S>1) THEN 
                 WRITE(IUN,'("STANDARDIZED TAU (WS variance parameters: log-linear model)")')
             ELSE
!                 WRITE(IUN,'("TAU (WS variance parameters: log-linear model)")')
                 WRITE(IUN,'("TAU (WS variance (level-1) parameters: log-linear model)")')
             END IF 
             DO L=1,S_cycle
                L2 = nmeaneff+L
                ZVAL = TAU(L)/SE(L2)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)TLAB(L),TAU(L),SE(L2),ZVAL,PVAL
             END DO

            if(t_cycle > 0) then        
                if(gammatrans .eq. 0) then
                    if(waves .ne. 1) then
                         IF (NCENT==1 .AND. T_cycle>0) THEN 
                             WRITE(IUN,'("STANDARDIZED GAMMA (Cluster-level parameters: log-linear model)")')
                         ELSE if(t_cycle > 0) then
                             WRITE(IUN,'("GAMMA (Cluster-level parameters: log-linear model)")')
                         END IF 
                     else
                         IF (NCENT==1 .AND. T_cycle>0) THEN 
                             WRITE(IUN,'("STANDARDIZED GAMMA (Wave-level parameters: log-linear model)")')
                         ELSE if(t_cycle > 0) then
!                             WRITE(IUN,'("GAMMA (Wave-level parameters: log-linear model)")')
                             WRITE(IUN,'("GAMMA (WS wave variance (level-2) parameters: log-linear model)")')
                         END IF 
                    end if                 
                     DO L=1,t_cycle
                        L2 = nmeaneff+L-t_cycle
                        ZVAL = gamma(L)/SE(L2)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,804)gLAB(L),gamma(L),SE(L2),ZVAL,PVAL
                    END DO
                else
                    if(waves .ne. 1) then
                        WRITE(IUN,'("CLUSTER-LEVEL RANDOM LOCATION EFFECT Variance")')
                     else
!                        WRITE(IUN,'("WAVE-LEVEL RANDOM LOCATION EFFECT Variance")')
                        WRITE(IUN,'("WAVE (level-2) Variance")')
                    end if                 
                    ZVAL = 1/SE(nmeaneff)
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)gLAB(1),exp(gamma(1)),SE(nmeaneff)*exp(gamma(1)),ZVAL,PVAL
                end if
            end if
            if(ns_cycle > 0) then
                if(ncov_cycle == 0) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT STANDARD DEVIATION")')
                else if(chol .ne. 2 .and. chol .ne. 1) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT Variance Matrix")')
                else
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT Cholesky Matrix")')
                end if
                n=0
                do k=1, numrs
                    if(ns_cycle .eq. numrs2) then
                        do m=1, k
                            n = n + 1
                            L2=nmeaneff+S_cycle+n
                            ZVAL = SPAR(n)/SE(L2)
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k) then
                                WRITE(IUN,804)rslabel(k),SPAR(n),SE(L2),ZVAL,PVAL
                            else
                                WRITE(IUN,805)"Cholesky",m+numloc,k+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                            end if
                        end do
                    else
                        do m=1, numloc+k
                            n = n + 1
                            mycoef = sigma(myorderinv(n+numloc2))
                            L2=nmeaneff+S_cycle+n
                            ZVAL = mycoef/SE(L2)
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k+numloc) then
                                WRITE(IUN,804)rslabel(k),mycoef,SE(L2),ZVAL,PVAL
                            else if(chol .eq. 0) then
                                WRITE(IUN,806)"Covariance",m,k+numloc,mycoef,SE(L2),ZVAL,PVAL
                            else if(ncov_cycle<2) then
                                ZVAL = spar(n)/SE(L2)
                                WRITE(IUN,805)"Cholesky",m,k+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                            end if
                        end do
                    end if
                end do
                    if(ncov_cycle==2) then
!                        WRITE(IUN,'("Random quadratic location (mean) effects on WS variance")')
                        ZVAL = SPAR(1)/SE(npar_cycle-2)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,809)'Linear loc interact ',SPAR(1),SE(npar_cycle-2),ZVAL,PVAL
                        ZVAL = SPAR(3)/SE(npar_cycle)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,809)'Quad loc interact   ',SPAR(3),SE(npar_cycle),ZVAL,PVAL
                    end if
            end if
         804 FORMAT(A16,4x,4(4x,F10.4))
         805 FORMAT(A8,1x,i1,1x,i1,8x,4(4x,F10.4))
         806 FORMAT(A10,1x,i1,1x,i1,6x,4(4x,F10.4))
         807 FORMAT(A24,4(F10.4,4x))
         809 FORMAT(A20,4(4x,F10.4))
        
             ! write out the deviance, estimates, and standard errors
             IF (CYCLES.EQ.1) OPEN(2, FILE='mixREGLS_3level_.EST')
                WRITE(2,'(F15.6,2I8)') -2*LOGL,ITER-1,MAXIT
                 WRITE(2,'(35F15.8)')(BETA(L),L=1,P)
                 if(mls .eq. 0) then
                    WRITE(2,'(35F15.8)')(alpha(L),L=1,nalpha)
                else
                    if(chol < 2) then
                        WRITE(2,'(35F15.8)')(sigma(myorderinv(L)),L=1,RR)
                    else
                        WRITE(2,'(35F15.8)')(mychol(L),L=1,RR)
                    end if
                end if
                 WRITE(2,'(35F15.8)')(gamma(L),L=1,t_cycle)
                 WRITE(2,'(35F15.8)')(TAU(L),L=1,S_cycle)
                 if(chol == 0) then
                    write(2,'(35F15.8)')(sigma(myorderinv(L+numloc2)),L=1,ns_cycle)
                else
                    write(2,'(35F15.8)')(spar(L),L=1,ns_cycle)
                end if
                 WRITE(2,'(35F15.8)')(SE(L),L=1,NPAR_cycle)
        close(IUN)
        OPEN(UNIT=IUN,FILE="mixREGLS_3level2.OUT",access="append")
        END DO CYCLELOOP
     write(iun,*)
     write(iun,*)
         myz = 1.959964
     WRITE(IUN,'("Variance ratios and 95% CIs")')
     write(iun,'("------------------------------")')
     write(iun,*)
        WRITE(IUN,808) 'Variable                  ','Ratio','Lower','Upper'
        write(iun,808)'-----------------------','------------------','------------','------------'
    if(mls .ne. 1 .and. nalpha>1) then
             WRITE(IUN,'("ALPHA (BS variance parameters: exponentiated)")')
         DO L=2-rnint,nalpha
            L2 = P+L
            tauhat = exp(alpha(l))
            tauhatlow = exp(alpha(l)-myz*se(l2))
            tauhatup = exp(alpha(l)+myz*se(l2))
            WRITE(IUN,804)aLAB(L),tauhat, tauhatlow, tauhatup
         END DO
         write(iun,*)
    end if
    if(no3 .ne. 1 .and. t_cycle > 1) then
        if(waves .eq. 1) then
             WRITE(IUN,'("GAMMA (Wave variance parameters: exponentiated)")')
        else
             WRITE(IUN,'("GAMMA (Cluster variance parameters: exponentiated)")')
        end if        
         DO L=2-tnint,t
            L2 = nmeaneff-t+L
            tauhat = exp(gamma(l))
            tauhatlow = exp(gamma(l)-myz*se(l2))
            tauhatup = exp(gamma(l)+myz*se(l2))
            WRITE(IUN,804)gLAB(L),tauhat, tauhatlow, tauhatup
         END DO
         write(iun,*)
    end if
808 FORMAT(A20,3(4x,A10))
         WRITE(IUN,'("TAU (WS variance parameters: exponentiated)")')
     DO L=2-snint,S
        L2 = nmeaneff+L
        tauhat = exp(tau(l))
        tauhatlow = exp(tau(l)-myz*se(l2))
        tauhatup = exp(tau(l)+myz*se(l2))
        WRITE(IUN,804)TLAB(L),tauhat, tauhatlow, tauhatup
     END DO
    write(iun,*)
        if(extrars > 0) then
         WRITE(IUN,'("SCALE RANDOM EFFECT (exponentiated)")')
            L2=nmeaneff+S_cycle+ns_cycle
            tauhat = exp(spar(ns_cycle))
            tauhatlow = exp(spar(ns_cycle)-myz*se(l2))
            tauhatup = exp(spar(ns_cycle)+myz*se(l2))
            WRITE(IUN,804)rslabel(2),tauhat, tauhatlow, tauhatup
    write(iun,*)
        end if
     WRITE(IUN,'("Variances (all covariates=0) and 95% CIs")')
     write(iun,'("----------------------------------------")')
     write(iun,*)
        WRITE(IUN,808) 'Variable                ','Variance','Lower','Upper'
        write(iun,808)'---------------------','------------------','------------','------------'
    if(mls .ne. 1 .and. rnint .eq. 0) then
            tauhat = exp(alpha(1))
            tauhatlow = exp(alpha(1)-myz*se(p+1))
            tauhatup = exp(alpha(1)+myz*se(p+1))
            WRITE(IUN,807)"Between subjects        ",tauhat, tauhatlow, tauhatup
    end if
    if(no3 .ne. 1) then
            tauhat = exp(gamma(1))
            tauhatlow = exp(gamma(1)-myz*se(nmeaneff+1-t_cycle))
            tauhatup = exp(gamma(1)+myz*se(nmeaneff+1-t_cycle))
        if(waves .eq. 1 .and. tnint .eq. 0) then
            WRITE(IUN,807)"Between waves           ",tauhat, tauhatlow, tauhatup
        else if(tnint .eq. 0) then
            WRITE(IUN,807)"Between clusters        ",tauhat, tauhatlow, tauhatup
        end if
    end if
        L2 = nmeaneff+1
        tauhat = exp(tau(1))
        tauhatlow = exp(tau(1)-myz*se(l2))
        tauhatup = exp(tau(1)+myz*se(l2))
        if(snint .eq. 0) WRITE(IUN,807)"Within subjects         ",tauhat, tauhatlow, tauhatup
            L2=nmeaneff+S_cycle+numloc
        if(nors .eq. 0) then
            if(chol > 0) then
                tauhat = exp(2*spar(1+numloc))
                tauhatlow = exp(2*spar(1+numloc)-2*myz*se(l2))
                tauhatup = exp(spar(1+numloc)+2*myz*se(l2))
            else
                tauhat = exp(sigma(1+numloc))
                tauhatlow = exp(sigma(1+numloc)-myz*se(l2))
                tauhatup = exp(sigma(1+numloc)+myz*se(l2))
            end if            
        end if           
         CLOSE(IUN)
         CLOSE(IUNS)
         close(111)
         CLOSE(1)
         CLOSE(2)
         CLOSE(3)
         close(18)
    if(no3 .ne. 1) open(23,file=trim(fileprefix)//"_ebvar2.dat")
    if(no3 .eq. 1) open(23,file=trim(fileprefix)//"_ebvar.dat")
        do k=1,nlevel2
            if(waves == 0 .or. no3 == 1) then
                write(23,'(i14,25g23.4)') widni(k,1),(thetas(k,i), i=1,myqdim), ((thetavs(k,i,j),j=1,i),i=1,myqdim)
            else
                write(23,'(i14,25g23.4)') widni(k,1),(thetasc(k,i), i=1,mytdim), ((thetavsc(k,i,j),j=1,i),i=1,mytdim)
            end if
        end do
        close(23)
    if(no3 .ne. 1) then
        open(25,file=trim(fileprefix)//"_ebvar3.dat")
            do k=1,nlevel3
                if(waves == 1) then
                    write(25,'(i14,25g23.4)') idni(k,1),(thetasc(k,i), i=1,mytdim), ((thetavsc(k,i,j),j=1,i),i=1,mytdim)
                else
                    write(25,'(i14,25g23.4)') idni(k,1),(thetas(k,i), i=1,myqdim), ((thetavs(k,i,j),j=1,i),i=1,myqdim)
                end if
            end do
            close(25)
    end if

    OPEN(UNIT=IUN,FILE="mixREGLS_3level_final.OUT")
        if(logl < bestlogl) then
            WRITE(IUN,*) "-----------------------------------------------------------------------"
            WRITE(IUN,*) "WARNING: THIS MODEL IS LIKELY INACCURATE SINCE LOG LIKELIHOOD INCREASED!"
            WRITE(IUN,*) "-----------------------------------------------------------------------"
            WRITE(IUN,*) 
        end if
        WRITE(IUN,*) 
        WRITE(IUN,*) 
        WRITE(IUN,*) "-------------"
        WRITE(IUN,*) "MODEL RESULTS"
        WRITE(IUN,*) "-------------"
        WRITE(IUN,*) 
           WRITE(IUN,562)ITER-1,ORIDGE,LOGL,LOGL-NPAR, &
           LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nobs)),0-2*LOGL,0-2*(LOGL-NPAR), &
           0-2*(LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nobs)))
            WRITE(IUN,57)
        
             PVAL=0.0D0
             ZVAL=0.0D0
        
             IF (NCENT==1 .AND. P>1) THEN
                 WRITE(IUN,'("STANDARDIZED BETA (regression coefficients)")')
             ELSE
                 WRITE(IUN,'("BETA (regression coefficients)")')
             END IF
             DO L=1,P
                ZVAL = BETA(L)/SE(L)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)BLAB(L),BETA(L),SE(L),ZVAL,PVAL
             END DO

            if(nalpha>0 .and. chol>1) then
                 IF (NCENT==1) THEN
                     WRITE(IUN,'("STANDARDIZED ALPHA (BS variance parameters: log-linear model)")')
                 ELSE
                     WRITE(IUN,'("ALPHA (BS variance parameters: log-linear model)")')
                 END IF
                 DO L=1,nalpha
                    L2 = P+L
                    ZVAL = ALPHA(L)/SE(L2)
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,804)ALAB(L),ALPHA(L),SE(L2),ZVAL,PVAL
                 END DO
            else
                if(chol .ne. 2) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM LOCATION EFFECT Variance Matrix")')
                else
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM LOCATION EFFECT Cholesky Matrix")')
                end if
                k = 1
                do i=1,r
                    do j=1,i
                        mycoef = sigma(myorderinv(k))
                        ZVAL = mycoef/SE(k+p)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        if(i .eq. j) then
                            WRITE(IUN,804)alab(i),mycoef,SE(k+p),ZVAL,PVAL
                        else if(chol .ne. 2) then
                            WRITE(IUN,804)'Covariance      ',mycoef,SE(k+p),ZVAL,PVAL
                        else
                            WRITE(IUN,804)'Cholesky        ',mycoef,SE(k+p),ZVAL,PVAL
                        end if
                        k = k+1
                    end do
                END DO
            end if
             IF (NCENT==1 .AND. S>1) THEN 
                 WRITE(IUN,'("STANDARDIZED TAU (WS variance parameters: log-linear model)")')
             ELSE
!                 WRITE(IUN,'("TAU (WS variance parameters: log-linear model)")')
                 WRITE(IUN,'("TAU (WS variance (level-1) parameters: log-linear model)")')
             END IF 
             DO L=1,S_cycle
                L2 = nmeaneff+L
                ZVAL = TAU(L)/SE(L2)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)TLAB(L),TAU(L),SE(L2),ZVAL,PVAL
             END DO

            if(t_cycle > 0) then        
                if(gammatrans .eq. 0) then
                    if(waves .ne. 1) then
                         IF (NCENT==1 .AND. T_cycle>0) THEN 
                             WRITE(IUN,'("STANDARDIZED GAMMA (Cluster-level parameters: log-linear model)")')
                         ELSE if(t_cycle > 0) then
                             WRITE(IUN,'("GAMMA (Cluster-level parameters: log-linear model)")')
                         END IF 
                     else
                         IF (NCENT==1 .AND. T_cycle>0) THEN 
                             WRITE(IUN,'("STANDARDIZED GAMMA (Wave-level parameters: log-linear model)")')
                         ELSE if(t_cycle > 0) then
!                             WRITE(IUN,'("GAMMA (Wave-level parameters: log-linear model)")')
                             WRITE(IUN,'("GAMMA (WS wave variance (level-2) parameters: log-linear model)")')
                         END IF 
                    end if                 
                     DO L=1,t_cycle
                        L2 = nmeaneff+L-t_cycle
                        ZVAL = gamma(L)/SE(L2)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,804)gLAB(L),gamma(L),SE(L2),ZVAL,PVAL
                    END DO
                else
                    if(waves .ne. 1) then
                        WRITE(IUN,'("CLUSTER-LEVEL RANDOM LOCATION EFFECT Variance")')
                     else
!                        WRITE(IUN,'("WAVE-LEVEL RANDOM LOCATION EFFECT Variance")')
                        WRITE(IUN,'("WAVE (level-2) Variance")')
                    end if                 
                    ZVAL = 1/SE(nmeaneff)
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)gLAB(1),exp(gamma(1)),SE(nmeaneff)*exp(gamma(1)),ZVAL,PVAL
                end if
            end if
            if(ns_cycle > 0) then
                if(ncov_cycle == 0) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT STANDARD DEVIATION")')
                else if(chol .ne. 2 .and. chol .ne. 1) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT Variance Matrix")')
                else
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT Cholesky Matrix")')
                end if
                n=0
                do k=1, numrs
                    if(ns_cycle .eq. numrs2) then
                        do m=1, k
                            n = n + 1
                            L2=nmeaneff+S_cycle+n
                            ZVAL = SPAR(n)/SE(L2)
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k) then
                                WRITE(IUN,804)rslabel(k),SPAR(n),SE(L2),ZVAL,PVAL
                            else
                                WRITE(IUN,805)"Cholesky",m+numloc,k+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                            end if
                        end do
                    else
                        do m=1, numloc+k
                            n = n + 1
                            mycoef = sigma(myorderinv(n+numloc2))
                            L2=nmeaneff+S_cycle+n
                            ZVAL = mycoef/SE(L2)
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k+numloc) then
                                WRITE(IUN,804)rslabel(k),mycoef,SE(L2),ZVAL,PVAL
                            else if(chol .eq. 0) then
                                WRITE(IUN,806)"Covariance",m,k+numloc,mycoef,SE(L2),ZVAL,PVAL
                            else if(ncov_cycle<2) then
                                ZVAL = spar(n)/SE(L2)
                                WRITE(IUN,805)"Cholesky",m,k+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                            end if
                        end do
                    end if
                end do
                    if(ncov_cycle==2) then
!                        WRITE(IUN,'("Random quadratic location (mean) effects on WS variance")')
                        ZVAL = SPAR(1)/SE(npar_cycle-2)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,809)'Linear loc interact ',SPAR(1),SE(npar_cycle-2),ZVAL,PVAL
                        ZVAL = SPAR(3)/SE(npar_cycle)
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        WRITE(IUN,809)'Quad loc interact   ',SPAR(3),SE(npar_cycle),ZVAL,PVAL
                    end if
            end if
        
             ! write out the deviance, estimates, and standard errors
             IF (CYCLES.EQ.1) OPEN(2, FILE='mixREGLS_3level_.EST')
                WRITE(2,'(F15.6,2I8)') -2*LOGL,ITER-1,MAXIT
                 WRITE(2,'(35F15.8)')(BETA(L),L=1,P)
                 if(mls .eq. 0) then
                    WRITE(2,'(35F15.8)')(alpha(L),L=1,nalpha)
                else
                    if(chol < 2) then
                        WRITE(2,'(35F15.8)')(sigma(myorderinv(L)),L=1,RR)
                    else
                        WRITE(2,'(35F15.8)')(mychol(L),L=1,RR)
                    end if
                end if
                 WRITE(2,'(35F15.8)')(gamma(L),L=1,t_cycle)
                 WRITE(2,'(35F15.8)')(TAU(L),L=1,S_cycle)
                 if(chol == 0) then
                    write(2,'(35F15.8)')(sigma(myorderinv(L+numloc2)),L=1,ns_cycle)
                else
                    write(2,'(35F15.8)')(spar(L),L=1,ns_cycle)
                end if
                 WRITE(2,'(35F15.8)')(SE(L),L=1,NPAR_cycle)
     write(iun,*)
     write(iun,*)
         myz = 1.959964
     WRITE(IUN,'("Variance ratios and 95% CIs")')
     write(iun,'("------------------------------")')
     write(iun,*)
        WRITE(IUN,808) 'Variable                  ','Ratio','Lower','Upper'
        write(iun,808)'-----------------------','------------------','------------','------------'
    if(mls .ne. 1 .and. nalpha>1) then
             WRITE(IUN,'("ALPHA (BS variance parameters: exponentiated)")')
         DO L=2-rnint,nalpha
            L2 = P+L
            tauhat = exp(alpha(l))
            tauhatlow = exp(alpha(l)-myz*se(l2))
            tauhatup = exp(alpha(l)+myz*se(l2))
            WRITE(IUN,804)aLAB(L),tauhat, tauhatlow, tauhatup
         END DO
         write(iun,*)
    end if
    if(no3 .ne. 1 .and. t_cycle > 1) then
        if(waves .eq. 1) then
             WRITE(IUN,'("GAMMA (Wave variance parameters: exponentiated)")')
        else
             WRITE(IUN,'("GAMMA (Cluster variance parameters: exponentiated)")')
        end if        
         DO L=2-tnint,t
            L2 = nmeaneff-t+L
            tauhat = exp(gamma(l))
            tauhatlow = exp(gamma(l)-myz*se(l2))
            tauhatup = exp(gamma(l)+myz*se(l2))
            WRITE(IUN,804)gLAB(L),tauhat, tauhatlow, tauhatup
         END DO
         write(iun,*)
    end if
         WRITE(IUN,'("TAU (WS variance parameters: exponentiated)")')
     DO L=2-snint,S
        L2 = nmeaneff+L
        tauhat = exp(tau(l))
        tauhatlow = exp(tau(l)-myz*se(l2))
        tauhatup = exp(tau(l)+myz*se(l2))
        WRITE(IUN,804)TLAB(L),tauhat, tauhatlow, tauhatup
     END DO
    write(iun,*)
        if(extrars > 0) then
         WRITE(IUN,'("SCALE RANDOM EFFECT (exponentiated)")')
            L2=nmeaneff+S_cycle+ns_cycle
            tauhat = exp(spar(ns_cycle))
            tauhatlow = exp(spar(ns_cycle)-myz*se(l2))
            tauhatup = exp(spar(ns_cycle)+myz*se(l2))
            WRITE(IUN,804)rslabel(2),tauhat, tauhatlow, tauhatup
    write(iun,*)
        end if
     WRITE(IUN,'("Variances (all covariates=0) and 95% CIs")')
     write(iun,'("----------------------------------------")')
     write(iun,*)
        WRITE(IUN,808) 'Variable                ','Variance','Lower','Upper'
        write(iun,808)'---------------------','------------------','------------','------------'
    if(mls .ne. 1 .and. rnint .eq. 0) then
            tauhat = exp(alpha(1))
            tauhatlow = exp(alpha(1)-myz*se(p+1))
            tauhatup = exp(alpha(1)+myz*se(p+1))
            WRITE(IUN,807)"Between subjects        ",tauhat, tauhatlow, tauhatup
    end if
    if(no3 .ne. 1) then
            tauhat = exp(gamma(1))
            tauhatlow = exp(gamma(1)-myz*se(nmeaneff+1-t_cycle))
            tauhatup = exp(gamma(1)+myz*se(nmeaneff+1-t_cycle))
        if(waves .eq. 1 .and. tnint .eq. 0) then
            WRITE(IUN,807)"Between waves           ",tauhat, tauhatlow, tauhatup
        else if(tnint .eq. 0) then
            WRITE(IUN,807)"Between clusters        ",tauhat, tauhatlow, tauhatup
        end if
    end if
    L2 = nmeaneff+1
    tauhat = exp(tau(1))
    tauhatlow = exp(tau(1)-myz*se(l2))
    tauhatup = exp(tau(1)+myz*se(l2))
    if(snint .eq. 0) WRITE(IUN,807)"Within subjects         ",tauhat, tauhatlow, tauhatup
        L2=nmeaneff+S_cycle+numloc
    if(ns > 0) then
        if(chol > 0) then
            tauhat = exp(2*spar(1+numloc))
            tauhatlow = exp(2*spar(1+numloc)-2*myz*se(l2))
            tauhatup = exp(spar(1+numloc)+2*myz*se(l2))
        else
            tauhat = exp(sigma(1+numloc))
            tauhatlow = exp(sigma(1+numloc)-myz*se(l2))
            tauhatup = exp(sigma(1+numloc)+myz*se(l2))
        end if            
!        WRITE(IUN,804)"",tauhat, tauhatlow, tauhatup
    end if
    close(IUN)
	
    open(237,file="mixregls_3level_.var")
    do k=1,npar
        write(237,'(25f18.6)') (adjVar(k,j), j=1,npar)
    end do
    close(237)
END SUBROUTINE estimate_level3

subroutine callmixreg
    use mixregls32slope
    use procedures3
    implicit none
    integer::i,j,iun,counter,myio,k,kk
    real(kind=10)::temp,logl,pval,zval,se(p+rr+1),temp3(2+rr+numloc+p),v1(rr)
    CHARACTER(LEN=16)::templabel
    
    ndim = numloc + numrs
    open(10,file="mixregls_3level_temp_.dat")
    do i=1,nobs
        if(waves .eq. 1) then
            write(10,*) ids(i), y(i), (x(i,j),j=1,p), (u(i,j),j=1,numloc)
        else
            write(10,*) ids2(i), y(i), (x(i,j),j=1,p), (u(i,j),j=1,numloc)
        end if
    end do
    close(10)
    
    OPEN(1,FILE="mixreg.def")
    WRITE(1,'(18A4)') HEAD
    WRITE(1,'("mixregls_3level_temp_.dat")')
    write(1,'("mixregls_3level_temp_.out")')
    write(1,'("mixregls_3level_temp_.def")')
    write(1,'("mixregls_3level_temp_.ebv")')
        WRITE(1,'(5I3,E10.1E3, 9I3)') 1, 20, numloc+p+2, numloc, p, CONV, 0, 0, 0, 0, 1, 0, 1, 0, 0
        WRITE(1,'(20I3)') 1, 2
        write(1,'(20I3)') (2+p+j,j=1,numloc)
        write(1,'(20I3)') (2+j,j=1,p)
        write(1,*) ylabel
        write(1,*) (alab(j), j=1,numloc)
        write(1,*) (blab(j), j=1,p)

        allocate(beta(p))
        allocate(tau(s))
        allocate(mychol(RR))
        allocate(spar(ns))
        allocate(alpha(nalpha))
        tau = 0
        spar = 0
        if(ns > 0) spar(1) = abs(log(sdlv))
    ALLOCATE (THETAs(nsubj,ndim))
    ALLOCATE (thetavs(nsubj,ndim,ndim))
    thetas = 0
    thetavs = 0

       CALL SYSTEM("MIXREG.EXE > mixregls_3level_temp_")
       open(unit=2,file="mixreg.lik")
       read(2,*) logl, npar
       close(2)
       if(npar < p+rr+1) then
        write(*,*) 'MIXREG unable to estimate model. Program terminating.'
        stop
    end if
       open(unit=3,file="mixreg.est")
       do i=1,npar
            read(3,*) templabel, temp
            if(i .le. p) then
                beta(i) = temp
            else if (i .le. p+rr) then
                mychol(i-p) = temp
                if(mls .eq. 1) v1(i-p) = temp
            else if (i .eq. p+rr+1) then
                tau(1) = log(temp)
            end if
        end do
       close(1)

        if(mls .ne. 1) then       
            alpha = 0
            alpha(1) = log(mychol(1))
        end if
!        else
       
           open(unit=4,file="mixreg.var")
           do i=1,p
                read(4,*) (temp3(j), j=1,p)
                se(i) = sqrt(temp3(i))
           end do
           do i=1,rr+1
                read(4,*) (temp3(j), j=1,rr+1)
                se(i+p) = sqrt(temp3(i))
           end do
           close(1)
!           deallocate(temp3)
           
!           allocate(temp3(2+r+rr))
           open(unit=15,file="mixregls_3level_temp_.ebv")
           do i=1,nsubj
               kk=1
!               read(1,*) (temp3(j),j=1,2+numloc+rr)
               read(15,*) (temp3(j),j=1,2)
               read(15,*) (temp3(2+j),j=1,numloc)
               read(15,*) (temp3(2+j+numloc),j=1,rr)
!write(19,*) (temp3(j),j=1,2+numloc+rr)
               do j=1,numloc
                   thetas(i,j) = temp3(2+j)
                   thetavs(i,j,j) = temp3(2+numloc+kk)
                   kk=kk+1
                   do k=j+1,numloc
                        thetavs(i,j,k) = temp3(2+numloc+kk)
                        thetavs(i,k,j) = temp3(2+numloc+kk)
                        kk=kk+1
                   end do
               end do
!write(19,*) temp3(2+numloc+rr),thetavs(i,1,1)
           end do
           close(15)
!           deallocate(temp3)
!write(19,*) (thetavs(i,1,1),i=1,nclust)
           
        IUN    = 16
        OPEN(UNIT=IUN,FILE="mixregls_3level3.OUT")
                WRITE(IUN,*)
                WRITE(IUN,*)
                WRITE(IUN,'("------------------------------")')
                WRITE(IUN,'("Model without Scale Parameters")')
                WRITE(IUN,'("------------------------------")')

         ! WRITE RESULTS
    WRITE(IUN,562) LOGL,LOGL-NPAR, &
    LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)),0-2*LOGL,0-2*(LOGL-NPAR), &
    0-2*(LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)))
    562 FORMAT(1X,'Log Likelihood                 = ',F12.3,/, &
             1X,"Akaike's Information Criterion = ",F12.3,/, &
             1X,"Schwarz's Bayesian Criterion   = ",F12.3,//,&
             1X,"==> multiplied by -2             ",      /  &
             1X,'Log Likelihood                 = ',F12.3,/, &
             1X,"Akaike's Information Criterion = ",F12.3,/, &
             1X,"Schwarz's Bayesian Criterion   = ",F12.3,/)
        WRITE(IUN,57)
    57 FORMAT(/,'Variable',12x,'    Estimate',4X,'AsymStdError',4x, &
              '     z-value',4X,'     p-value',/,'----------------',4x,  &
              '------------',4X,'------------',4X,'------------',4X,'------------')

         PVAL=0.0D0
         ZVAL=0.0D0


        WRITE(IUN,'("BETA (regression coefficients)")')
         DO i=1,P
            ZVAL = BETA(i)/SE(i)
            PVAL = 2.0D0 *(1.0D0 - PHIFN(abs(ZVAL),0))
                WRITE(IUN,804)BLAB(i),BETA(i),SE(i),ZVAL,PVAL
         END DO
         write(iun,*)
        WRITE(IUN,'("Random (location) Effect Variances and Covariances")')
        counter = 1
         do i=1,numloc
             do j=1,i
                ZVAL = mychol(counter)/SE(counter+p)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(abs(ZVAL),0))
                if(i==j) then
                    WRITE(IUN,804)alab(i),mychol(counter),SE(counter+p),ZVAL,PVAL
                else
                    WRITE(IUN,805)'Covariance',j,i,mychol(counter),SE(counter+p),ZVAL,PVAL
                end if
                counter = counter+1
            end do
         END DO
        ZVAL = temp/SE(1+p+rr)
        PVAL = 2.0D0 *(1.0D0 - PHIFN(abs(ZVAL),0))
        write(iun,*)
        WRITE(IUN,804)'Error variance  ',temp,SE(1+p+rr),ZVAL,PVAL

        804 FORMAT(A16,4(4x,F12.5))
        805 FORMAT(A10,I0,I0,4X,4(4x,F12.5))
        close(iun)
           se(npar) = se(npar)/temp
        open(2,file="mixregls_3level_.est")
        WRITE(2,'(F15.6,2I8)') -2*LOGL,20,MAXIT
         WRITE(2,'(35F15.8)')(BETA(k),k=1,P)
         WRITE(2,'(35F15.8)')(mychol(k),k=1,RR)
         WRITE(2,'(35F15.8)')(TAU(1))
         WRITE(2,'(35F15.8)')(SE(k),k=1,1+p+rr)
         close(2)

!        v = mychol
           if(mls .eq. 1) call CHSKY(v1,mychol,r,myio)
!    end if
end subroutine callmixreg

subroutine run_stage2()
    use mixregls32slope
    implicit none
    integer::i,j,k
    
    open(1,file="stage2only_slope.def")
    WRITE(1,'(18A4)') HEAD
        if(sepfile .ne. 1) then
            filedat2 = filedat
            nvarsep = nvar
            id2indsep = id2ind
        end if
        write(1,*)FILEDAT2
        if(no3 .ne. 1) write(1,*)trim(fileprefix)//"_ebvar3.dat"
        if(no3 .eq. 1)  write(1,*)trim(fileprefix)//"_ebvar.dat"
        write(1,*)trim(FILEprefix)//"_stage2"
        if(numrs > 1) then
            write(1,*) NVARsep,nlevel3,numloc,nreps, numrs, myseed, stage2, multi2nd,slopecol,Ymiss
        else
            write(1,*) NVARsep,nlevel3,numloc,nreps, nors, myseed, stage2, multi2nd,slopecol,Ymiss
        end if        
        write(1,*) pfixed,ptheta,pomega,pto
        if(readcats .eq. 1) then
            write(1,*) maxj
            write(1,*)(iCODE(J), J = 1,MAXJ)
        end if
        write(1,*) id2indsep,var2ind(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,*) (var2ind(k+I), I=1,Pfixed)
            k = k + pfixed
        end if
        IF (Ptheta .GE. 1) THEN
            write(1,*) (var2IND(k+I), I=1,Ptheta)
            k = k + ptheta
        END IF
        IF (Pomega .GE. 1) THEN
            write(1,*) (var2IND(k+I), I=1,Pomega)
            k = k + pomega
        end if
        IF (Pto .GE. 1) THEN
            write(1,*) (var2IND(k+I), I=1,Pto)
        end if

        write(1,*) var2label(1)
        k = 1
        IF (Pfixed .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pfixed)
            k = k + pfixed
        end if
        IF (Ptheta .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Ptheta)
            k = k + ptheta
        end if
        IF (Pomega .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pomega)
            k = k + pomega
        end if
        IF (Pto .GE. 1) THEN
            write(1,*) (var2label(k+I), I=1,Pto)
        END IF
        call system("stage2only_slope")
    CLOSE(1)   
end subroutine run_stage2


SUBROUTINE estimate_level2()
    use mixregls32slope
    use procedures3
    implicit none
    INTEGER :: I,J,L,LL,l2,k,m,n,mynob,kk,ii,IUN,CYCLES,NCYCLE,IFIN,ITER, &
               Q,NOB,RIDGEIT,IUNS,myqdim,myqdim2,totalqR0,totalqR1,mytotalq,counter,cholcol,neg2nd,npar_cycle,ncov_cycle
    integer,allocatable :: myorder(:),myorder2(:),myorderinv(:),myorderinv2(:)
    REAL(kind=bigreal) :: RIDGE,LOGLP,PSUM,LOGL,XB,WT,ERRIJK,LPROB,LOGDIFF,MAXCORR,ONE,&
                    ORIDGE,PVAL,ZVAL,RTEMP,SMALL,BIG,LOGBIG,MAXDER,sdev,phiRatio,mytemp,&
                    myz,tauhat,tauhatlow,tauhatup,sigmae,errijk1,qprob,us,iprob,mycoef,myse,bestlogl,log2pi
    REAL(kind=bigreal),ALLOCATABLE:: myder(:),myder2(:,:),LIK(:,:),myider(:), DERQ1(:,:),DERQ2(:,:),DZ1(:,:),COREC1(:,:), &
                               theta1(:,:),dz1r(:,:),se(:),qprobs(:),thetav(:,:),WORK2(:),WORK3(:), temp(:,:),&
                               chol2(:,:),u2var(:,:),u2mean(:,:),pointsR0(:,:),weightsR0(:),pointsR1(:,:),weightsR1(:),corec(:),&
                               varSQ(:,:),myider1(:,:),myider2(:,:),tempmat(:,:),tempmat2(:,:),&
                               work(:,:),adjvar(:,:),asstar2(:,:),sigma(:),mycholspar(:),mycholspar2(:)!,points1(:,:),weights1(:),
    character(len=80)::templabel

    open(37,file=trim(fileprefix)//"_ebnames.txt")
    write(37,*) "ID"
    do i=1,numloc
        write(templabel,'(a9,i1)') "Location_",i
        write(37,*) templabel
    end do
    do i=1,numrs
        write(templabel,'(a6,i1)') "Scale_",i
        write(37,*) templabel
    end do
    do i=1,numloc+numrs
        do j=1,i
            write(templabel,'(a4,i1,i1)') "pvar",i,j
            write(37,*) templabel
        end do
    end do
    close(37)
    
        ! parameters
    bestlogl  = -999999999999999.0
    log2pi = log(2*3.141592653589793238462643d0)   
    ONE = 1.0D0
    RTEMP = 1.0D0
    SMALL  = TINY(RTEMP)
    BIG    = HUGE(RTEMP)
    LOGBIG   = LOG(BIG)
    ndim = numloc + numrs
    ndim2 = (ndim+1)*ndim/2
    numloc2 = (numloc+1)*numloc/2
    IUN    = 16
    OPEN(UNIT=IUN,FILE="mixREGLS_3level2.OUT")
    IUNS    = 17
    OPEN(UNIT=IUNS,FILE="mixREGLS_3level_details_.ITS")
    open(unit=18, file="MixRegls_3level_.its")
    totalqR1 = NQ**(numloc+numrs)
    totalqR0 = nq**numloc
    allocate(weightsR1(totalqR1))
    allocate(pointsR1(totalqR1,numloc+numrs))
    allocate(weightsR0(totalqR0))
    allocate(pointsR0(totalqR0,numloc))
    call getquad(numloc, nq, totalqR0, pointsR0, weightsR0)
    call getquad(numloc+numrs, nq, totalqR1, pointsR1, weightsR1)
!    call getquad(1, nq, nq, points1, weights1)
    ! number of quadrature points, quadrature nodes & weights
    myqdim = ndim
    allocate(myorder(ndim2))
    allocate(myorder2(ndim2))
    allocate(myorderinv(ndim2))
    allocate(myorderinv2(ndim2))
        !Establishing arrays at maximum size needed
        ! NS = number of additional var cov parameters due to random SCALE
    NS = ndim2-numloc2
    if(ncov .ne. 1) ns = numrs2 + ncov
    npar = p+s+ns+RR*mls+nalpha
    NPAR2 = NPAR*(NPAR+1)/2
    allocate(myder(npar))
    allocate(myder2(npar,npar))
    allocate(corec(npar))
    allocate(corec1(npar,1))
    allocate(myider1(npar,1))
    allocate(myider(npar))
    allocate(myider2(npar,npar))
    allocate(tempmat(npar,npar))
    allocate(tempmat2(npar,npar))
    ALLOCATE (SE(NPAR))
    ALLOCATE (THETA1(ndim,1))
    ALLOCATE (thetav(ndim,ndim))
    cholamt=ndim2
    allocate(sstar(cholamt,cholamt))
    allocate(work(npar,npar))
    allocate(adjVar(npar,npar))
    allocate(asstar2(npar,npar))
    allocate(sigma(cholamt))
    allocate(mycholspar(cholamt))
    allocate(mycholspar2(cholamt))
    allocate(qprobs(totalqR1))
    allocate(u2mean(ndim,1))
    allocate(u2var(ndim,ndim))
    allocate(chol2(ndim,ndim))
    ALLOCATE (LIK(nsubj,totalqR1))
    ALLOCATE (DERQ1(NPAR,1))
    ALLOCATE (DERQ2(NPAR,npar))
    ALLOCATE (DZ1(NPAR,1))
    ALLOCATE (DZ1r(NPAR,1))
    ALLOCATE (temp(NPAR,npar))
    ALLOCATE (WORK2(ndim))
    ALLOCATE (WORK3(ndim2))
    allocate(myweights(totalqR1))
    allocate(mypoints(totalqR1,ndim))
    allocate(myweights0(totalqR1))
    allocate(mypoints0(totalqR1,ndim))
    allocate(varSQ(npar,npar))
 
    counter = 1
    do j=0,ndim-1
        do i=1,ndim-j
            myorder(counter) = (j+i)*(j+i-1)/2 + j + 1
            myorderinv(myorder(counter)) = counter
            counter = counter + 1
        end do
    end do
    do j=1,cholamt
        mycholspar2(j) = j
    end do
        ! start cycles
        ! cycles = 1: random intercept model with BS variance terms
        ! cycles = 2: add in scale (WS) variance terms
        ! cycles = 3: add in random scale 
        ! cycles = 4: use NS = R+1

    corec = 0        
    s_cycle = 1
    ns_cycle = 0
    ncov_cycle = 0
    ncycle = 4
    if(ncov .eq. 0) ncycle = 3
    if(nors .eq. 1) ncycle = 2
    CYCLELOOP:do cycles=1,ncycle
        if (cycles==1) then
            myqdim = numloc
            mytotalq = totalqR0
            mypoints0 = pointsR0
            myweights0 = weightsR0
            if(mls .eq. 1) cycle
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("------------------------------")')
            WRITE(IUN,'("Model without Scale Parameters")')
            WRITE(IUN,'("------------------------------")')
            WRITE(*,*)
            WRITE(*,*) "------------------------------"
            WRITE(*,*) "Model without Scale Parameters"
            WRITE(*,*) "------------------------------"
            WRITE(IUNS,*)
            WRITE(IUNS,*) "------------------------------"
            WRITE(IUNS,*) "Model without Scale Parameters"
            WRITE(IUNS,*) "------------------------------"
        else if (CYCLES==2) THEN
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("---------------------------")')
            WRITE(IUN,'("Model WITH Scale Parameters")')
            WRITE(IUN,'("---------------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("---------------------------")')
            WRITE(IUNS,'("Model WITH Scale Parameters")')
            WRITE(IUNS,'("---------------------------")')
            WRITE(*,*)
            WRITE(*,*) "---------------------------"
            WRITE(*,*) "Model WITH Scale Parameters"
            WRITE(*,*) "---------------------------"
            s_cycle = s
            myqdim = numloc
        else if (CYCLES==3) THEN
            WRITE(IUN,*)
            WRITE(IUN,*)
            WRITE(IUN,'("-----------------------")')
if(ncov .eq. 0) WRITE(IUN,'("Model WITH RANDOM Scale")')
if(ncov .eq. 0) WRITE(IUN,'("-----------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("-----------------------")')
            WRITE(IUNS,'("Model WITH RANDOM Scale")')
            WRITE(IUNS,'("-----------------------")')
            WRITE(*,*)
            WRITE(*,*) "-----------------------"
            WRITE(*,*) "Model WITH RANDOM Scale"
            WRITE(*,*) "-----------------------"
            NS_cycle = numrs2 ! number of additional var cov parameters due to random SCALE
            myqdim = numloc+numrs
            mytotalq = totalqR1
            mypoints0 = pointsR1
            myweights0 = weightsR1
            spar(1:ns_cycle) = 0
            spar(1) = .2
            spar(numrs2) = .2
        else if (CYCLES==4) THEN
!            WRITE(IUN,*)
!            WRITE(IUN,*)
!            WRITE(IUN,'("-----------------------")')
            WRITE(IUN,'("Model WITH correlated RANDOM Scale")')
            WRITE(IUN,'("-----------------------")')
            WRITE(IUNS,*)
            WRITE(IUNS,'("-----------------------")')
            WRITE(IUNS,'("Model WITH correlated RANDOM Scale")')
            WRITE(IUNS,'("-----------------------")')
            WRITE(*,*)
            WRITE(*,*) "-----------------------"
            WRITE(*,*) "Model WITH correlated RANDOM Scale"
            WRITE(*,*) "-----------------------"
            if(numrs > 1) then
                spar((ns-numrs+1):ns) = spar(2:numrs2)
                spar(2:numrs2) = 0
            end if
            NS_cycle = ns
            ncov_cycle = ncov
            spar(numloc+1) = spar(1)
            spar(1:numloc) = 0
        end if
        NPAR_cycle = P+RR*mls+nalpha+S_cycle+NS_cycle
        nmeaneff = p+rr*mls+nalpha
!write(iuns,*) npar_cycle, numrs, nmeaneff, ns_cycle, ns, ndim2, numloc2
        myqdim2 = myqdim*(myqdim+1)/2
    ! start iterations
    !

         ! ifin = 1 for iterations using NR (or BHHH depending on the inversion of DER2)
         !      = 2 for final iteration
         !

        ridge  = ridgein
        RIDGEIT= 0

         ! set the ridge up for the models with scale parameters
        if (CYCLES<=2) then
            ridge = ridgein+.05D0
        end if
        if (CYCLES==3) THEN
            ridge = RIDGEIN+.3D0
        end if
        if(cycles .eq. 4) ridge = ridge + .3
        if(ridge < 0) ridge = ridgein

        loglp  = -999999999999999.0

        ITER=1
    ! START WITH NEWTON RAPHSON (MIXREG for starting values)
        IFIN=1
        IFINLOOP:DO WHILE (ifin < 3)

             ! set ifin=2 if on the last iteration
            if (ifin == 2) then
                ifin = 3
            end if

             ! put the ridge back to its initial value after the first 5 & 10 iterations
!            IF (CYCLES>=3 .AND. ITER==11) THEN
!                ridge = ridge - .2
!             else 
            if(ridge < 0) ridge = 0 !To get rid of -0 values
             
        !
        ! calculate the derivatives and information matrix
        !
            LOGL= 0.0D0
            myDER(1:npar_cycle)=0.0D0
            myder2(1:npar_cycle,1:npar_cycle) = 0.0D0
            nob = 0
            mynob = 0

            ILOOP:DO I=1,nsubj  ! go over subjects
                theta1 = 0
                thetav = 0
                mypoints = mypoints0
                myweights = myweights0
                if(myqdim>0 .and. aquad .ne. 0 .and. (iter >= 10 .or. cycles .ne. 3)) then
                    do k=1,myqdim
                        sdev = sqrt(abs(thetavs(i,k,k)))
                        do q=1,mytotalq
                            mytemp = mypoints(q,k)
                            mypoints(q,k) = thetas(i,k) + sdev*mypoints(q,k)
                            call get_phi_ratio(mypoints(q,k),mytemp, phiRatio)
                            myweights(q) = sdev*phiRatio*myweights(q)
                        end do
                    end do
                end if
                iprob = 0
                myider1(1:npar_cycle,1) = 0
                myider2(1:npar_cycle,1:npar_cycle) = 0

                QLOOP:DO Q=1, mytotalq  ! go over quadrature points
                    DERQ1(1:npar_cycle,1)  = 0.0D0
                    DERQ2(1:npar_cycle,1:npar_cycle) = 0.0D0
                    PSUM     = 0.0D0
                    JLOOP: do j=1,idni(i,2)
                        nob = mynob + j
!write(iuns,*) i,mynob,j,nob                                   
                        DZ1(1:npar_cycle,1)    = 0.0D0

                        XB = DOT_PRODUCT(BETA,X(NOB,:))        ! X BETA for the current LEVEL-1 obs
                        WT = DOT_PRODUCT(TAU(1:S_cycle),W(NOB,1:S_cycle))  ! W TAU for the current LEVEL-1 obs
                        if (ns_cycle > 0) then
                            n=0
                            do k=1, numrs
                                if(ns_cycle .eq. numrs2) then
                                    do m=1, k
                                        n = n + 1
                                        wt = wt + rsvar(nob,k) * spar(n) * mypoints(q, numloc+m)
                                    end do
                                else
                                    do m=1, numloc+k
                                        n = n + 1
                                        wt = wt + rsvar(nob,k) * spar(n) * mypoints(q, m)
!write(iuns,*) mychol(t),u(nob,k),us,mypoints(q,m)
                                    end do
                                end if
                            end do
                            if(ncov_cycle .eq. 2) then
!                                wt = wt + spar(ns_cycle-1) * mypoints(q, 1)
                                wt = wt + spar(ns_cycle) * mypoints(q, 1)**2
                            end if
                        end if    
!Covariance between location and scale effects not currently included
                !     Random effect term (linear predictor?)
                        Us = 0  ! Either Ualpha or UsigmaTheta1 depending on model
                        if(rr*mls > 0) then
                            n=0
                            do k=1, numloc
                                do m=1, k
                                    n = n + 1
                                    Us = Us + U(nob,k) * mychol(n) * mypoints(q, m)
!write(iuns,*) mychol(t),u(nob,k),us,mypoints(q,m)
                                end do
                            end do
                        else if(nalpha > 0) then
!write(iuns,*) nalpha,alpha(1:nalpha),u(nob,1:nalpha),mypoints(q,1),dot_product(alpha(1:nalpha),U(nob,1:nalpha))
                            if(dot_product(alpha(1:nalpha),U(nob,1:nalpha)) < logbig) then
                                Us = mypoints(q,1) * exp(.5*dot_product(alpha(1:nalpha),U(nob,1:nalpha)))
                            else
                                Us = big
                            end if
                        end if
                        IF (wt .GE. LOGBIG) THEN
                            sigmae = BIG
                        ELSE
                            sigmae = EXP(.5*wt)
                        END IF
                        IF (sigmae .LE. SMALL) sigmae = SMALL   
                        errijk = y(nob) - (xb + us)
                        errijk1 = errijk/sigmae
                        lprob = -.5*log2pi - log(sigmae) - .5*errijk**2/sigmae**2
                        PSUM  = PSUM + LPROB
!write(iuns,*) j,q,mypoints0(q,1),mypoints(q,1), errijk1,lprob,psum
                        ! GET FIRST DERIVATIVES
                        DZ1(1:P,1) = ERRIJk1*X(NOB,1:p)/sigmae                    ! beta
!                                DZ1(1:P,1) = ERRIJk*X(NOB,1:p)/sigmae**2                    ! beta
                        if(mls .eq. 1) then
                            n=0
                            do k=1, numloc
                                do m=1, k
                                    n = n + 1
                                    dz1(p+n,1) = U(nob,k) * mypoints(q, m)*errijk1/sigmae    !T
!                                            dz1(p+n,1) = U(nob,k) * mypoints(q, m)*errijk/sigmae**2
                                end do 
                            end do
                        else
                            DZ1(P+1:P+nalpha,1) = ERRIJk1/sigmae*Us*U(NOB,1:nalpha)/2 ! alpha
!                                    DZ1(P+1:P+nalpha,1) = (ERRIJk/sigmae**2)*Us*U(NOB,1:nalpha)/2
                        end if
                        DZ1((nmeaneff+1):(nmeaneff+S_cycle),1) = (-1 +ERRIJk1**2)*W(NOB,1:S_cycle)/2    ! tau
!                                if(ns_cycle > numrs) dz1(p+rr*mls+nalpha+s_cycle+1:p+rr*mls+nalpha+s_cycle+ns_cycle,1) &
!                                                    = ERRIJk1**2*mypoints(q,1:ns_cycle) ! sparam
                        if (ns_cycle > 0) then
                            n=0
                            do k=1, numrs
                                if(ns_cycle .eq. numrs2) then
                                    do m=1, k
                                        n = n + 1
                                        dz1(nmeaneff+s_cycle+n,1) = (-1+ERRIJk1**2)*rsvar(nob,k) * mypoints(q, numloc+m)/2
                                    end do
                                else
                                    do m=1, numloc+k
                                        n = n + 1
                                        dz1(nmeaneff+s_cycle+n,1) = (-1+ERRIJk1**2)*rsvar(nob,k) * mypoints(q, m)/2
                                    end do
                                end if
                            end do
                            if(ncov_cycle .eq. 2) dz1(npar_cycle,1) = (-1+ERRIJk1**2)*rsvar(nob,1) * mypoints(q, 1)**2/2
!                                if(ns_cycle .eq. numrs) dz1(k+s_cycle+1:numrs,1) = &
!                                                        (1-ERRIJk1**2)*mypoints(q,(myqdim-numrs+1):myqdim)/2 ! sparam
!                            if(ncov_cycle .eq. 2) then
!                                dz1r(npar_cycle-1,1) = ERRIJk1*mypoints(q, 1)
!                            end if
                        end if
                        DERQ1(1:npar_cycle,1) = DERQ1(1:npar_cycle,1) + DZ1(1:npar_cycle,1)

!if(iter==1) write(iuns,*) i,q,j,psum,(dz1(k,1),k=1,npar_cycle)
                        dz1r(1:npar_cycle,1) = dz1(1:npar_cycle,1)/errijk1
                        DZ1r((nmeaneff+1):(nmeaneff+S_cycle),1) = (ERRIJk1)*W(NOB,1:S_cycle)!sqrt(2.)    ! tau
                        if (ns_cycle > 0) then
                            n=0
                            do k=1, numrs
                                if(ns_cycle .eq. numrs2) then
                                    do m=1, k
                                        n = n + 1
                                        dz1r(nmeaneff+s_cycle+n,1) = ERRIJk1*rsvar(nob,k) * mypoints(q, numloc+m)!/sqrt(2.)
                                    end do
                                else
                                    do m=1, numloc+k
                                        n = n + 1
                                        dz1r(nmeaneff+s_cycle+n,1) = ERRIJk1*rsvar(nob,k) * mypoints(q, m)!/sqrt(2.)
                                    end do
                                end if
                            end do
                            if(ncov_cycle .eq. 2) dz1r(npar_cycle,1) = ERRIJk1*mypoints(q, 1)**2
                        end if
                        tempmat(1:npar_cycle,1:npar_cycle) = matmul(dz1r(1:npar_cycle,:),transpose(dz1r(1:npar_cycle,:)))
!write(67,*) "temp0",(tempmat(k,k),k=1,5),(tempmat(5,k),k=1,5)
                        tempmat(nmeaneff+1:npar_cycle,nmeaneff+1:npar_cycle) = &
                                                               tempmat(nmeaneff+1:npar_cycle,nmeaneff+1:npar_cycle)/2
                        if(nalpha > 0) then
                            tempmat(p+1:p+nalpha,p+1:p+nalpha) = tempmat(p+1:p+nalpha,p+1:p+nalpha) &
                                                            - matmul(dz1(p+1:p+nalpha,:),u(nob:nob,1:nalpha))/2
                        end if
                        derq2(1:npar_cycle,1:npar_cycle) = derq2(1:npar_cycle,1:npar_cycle) - &
                                                            tempmat(1:npar_cycle,1:npar_cycle)
!if(i==1) write(IUNS,'(3i4,17F9.3)') i,q,j,mypoints(q,1),lprob,psum,errijk1, y(nob),xb,us,wt, (dz1r(k,1),k=1,npar_cycle)                
!if(i==1) write(IUNS,'(3i4,17F9.3)') i,q,j,mypoints(q,1),lprob,psum,errijk1, y(nob),xb,us,wt, (derq1(k,1),k=1,npar_cycle)                
                    end do jloop
                    qprob = exp(psum)*myweights(q)
                    qprobs(q) = qprob
                    iprob = iprob + qprob
                    myider1(1:npar_cycle,1) = myider1(1:npar_cycle,1) + derq1(1:npar_cycle,1)*qprob
 !if(iter==1) write(iuns,'(2i3,28g14.3)') i,q,psum,exp(psum),myweights(q),qprob,iprob,(derq1(k,1),k=1,npar_cycle), &
! (myider1(k,1),k=1,npar_cycle)
!if(cycles > 2) write(iuns,*) qprob,(derq1(k,1),k=1,npar_cycle)
                    myider2(1:npar_cycle,1:npar_cycle) = myider2(1:npar_cycle,1:npar_cycle) + &
                                                    derq2(1:npar_cycle,1:npar_cycle)*qprob + &
                                                    matmul(derq1(1:npar_cycle,:),transpose(derq1(1:npar_cycle,:)))*qprob
!write(67,*) "qprob",qprob, "derq2",(derq2(k,k),k=1,5),(derq2(5,k),k=1,5)
                    if(myqdim > 0) theta1(1:myqdim,1) = theta1(1:myqdim,1) + qprob*mypoints(q,1:myqdim)
!write(IUNS,'(2i4,17F11.5)') i,q,(derq1(k,1),k=1,npar_cycle),psum,qprob,myweights(q),iprob                
!write(IUNS,'(2i4,17F11.5)') i,q,(myider1(k,1),k=1,npar_cycle)                
                END DO qLOOP
                IF (iprob .LE. SMALL) iprob = SMALL   
                myider1(1:npar_cycle,1) = myider1(1:npar_cycle,1)/iprob
                myder(1:npar_cycle) = myder(1:npar_cycle) + myider1(1:npar_cycle,1)
                logl = logl + log(iprob)
!if(iter==1) write(iuns,'(1i3,28g14.3)') i,log(iprob),iprob,logl,(myider1(k,1),k=1,npar_cycle),(myder(k),k=1,npar_cycle)
                tempmat(1:npar_cycle,1:npar_cycle) = matmul(myider1(1:npar_cycle,:),transpose(myider1(1:npar_cycle,:)))
                tempmat2(1:npar_cycle,1:npar_cycle) = - myider2(1:npar_cycle,1:npar_cycle)/iprob + &
                                                        tempmat(1:npar_cycle,1:npar_cycle)
!write(67,'(i3,15g10.2)') i,tempmat(5,1),tempmat(1,5),tempmat2(1,5),tempmat2(5,1),myder2(1,5),myder2(5,1) 
                myder2(1:npar_cycle,1:npar_cycle) = myder2(1:npar_cycle,1:npar_cycle) + tempmat2(1:npar_cycle,1:npar_cycle)
!write(67,'(i3,10g10.2)') i,(myder2(k,k),k=1,5),(myder2(5,k),k=1,5)
!write(67,'(i3,15g10.2)') i,tempmat(5,1),tempmat(1,5),tempmat2(1,5),tempmat2(5,1),myder2(1,5),myder2(5,1)
                if(myqdim > 0) then
                    theta1(1:myqdim,1) = theta1(1:myqdim,1) / iprob
                    do q=1,mytotalq
                        thetav(1:myqdim,1:myqdim) = thetav(1:myqdim,1:myqdim) + qprobs(q) * &
                            matmul(theta1(1:myqdim,1:1)-transpose(mypoints(q:q,1:myqdim)), &
                            transpose(theta1(1:myqdim,1:1))-mypoints(q:q,1:myqdim))
                    end do
                    thetav(1:myqdim,1:myqdim) = thetav(1:myqdim,1:myqdim) / iprob
                    thetas(i,1:myqdim) = theta1(1:myqdim,1)
                    thetavs(i,1:myqdim,1:myqdim) = thetav(1:myqdim,1:myqdim)
                end if
                mynob = mynob + idni(i,2)
            END DO ILOOP

            LOGDIFF = LOGL-LOGLP
            LOGLP   = LOGL

                 ! determine if an NR iteration is bad and increase the ridge
                 ! take the ridge off after 10 good iterations
            if (LOGDIFF/LOGLP > .005 .AND. ITER < MAXIT) THEN
                RIDGEIT = 0
                RIDGE = RIDGE + .1D0
                WRITE(IUNS,'("==> BAD NR ITERATION ",I5," with NEW ridge = ",F8.4,2g16.6,/)') ITER,RIDGE,-2*logl,logdiff/loglp
                corec(1:npar_cycle) = -.5 * corec(1:npar_cycle)
                GO TO 99
            end if
            if (LOGDIFF/LOGLP <= .000001 .AND. RIDGEIT < 10) THEN
                RIDGEIT = RIDGEIT+1
             ELSE IF (LOGDIFF/LOGLP <= .000001 .AND. RIDGEIT >= 10 .and. ifin==1) then
                ridge = ridge - .1
                if(ridge < 0) ridge = 0
                ridgeit=0
            end if
            if(maxder < 2 .and. iter > 10) ridge = 0

            neg2nd = 0
            write(IUNS,*)"2nd Derivatives without ridge"
            do k=1,npar_cycle
                write(IUNS,'(25g15.4)') (myder2(k,i), i=1,k)
                if(myder2(k,k) < 0) neg2nd = neg2nd + 1
            end do
            if(ifin < 2) then
                 ! ridge adjustment - diagonal elements only
                do k=1,npar_cycle
                    myder2(k,k) = abs(myder2(k,k))*(1 + ridge)
                end do
            end if
        
            temp(1:npar_cycle,1:npar_cycle) = myder2(1:npar_cycle,1:npar_cycle)
            call inverse(temp(1:npar_cycle,1:npar_cycle), temp(1:npar_cycle,1:npar_cycle), npar_cycle)
!    write(IUNS,*)"Information Matrix"
!                do k=1,npar_cycle
!                    write(IUNS,'(25g15.4)') (temp(k,i), i=1,npar_cycle)
!                end do
            corec1(1:npar_cycle,1) = myder(1:npar_cycle)
            corec1(1:npar_cycle,1) = matmul(temp(1:npar_cycle,1:npar_cycle), corec1(1:npar_cycle,1))
            if(iter<=5 .and. cycles > 1) corec1(1:npar_cycle,1) = corec1(1:npar_cycle,1)*.5
            if(neg2nd > 2) corec1(1:npar_cycle,1) = corec1(1:npar_cycle,1)*.5
            corec(1:npar_cycle) = corec1(1:npar_cycle,1)


            write(IUNS,*)"Corrections"
            write(IUNS,'(20f11.3)') (corec(k), k=1,npar_cycle)
            write(IUNS,*)"Derivatives"
            write(IUNS,'(20f11.3)') (myder(k), k=1,npar_cycle)

            MAXDER=MAXVAL(ABS(myDER(1:npar_cycle)))
            MAXCORR=MAXVAL(ABS(COREC(1:npar_cycle)))
            WRITE(*,*) iter,'  maximum correction and derivative and ridge'
            WRITE(*,'(25g15.4)') maxcorr,MAXDER,ridge
            WRITE(IUNS,*) iter,'  maximum correction and derivative and ridge'
            WRITE(IUNS,'(25g15.4)') MAXCORR,MAXDER,ridge
        
             ! done with NR and onto last iteration
            if (IFIN==1 .AND. (MAXCORR <= CONV .OR. ITER >= MAXIT)) THEN
                ifIN=2
                 ORIDGE=RIDGE
                 RIDGE=0.0D0
            end if
        do k=1,npar_cycle
            if(corec(k) > 1) corec(k) = .5
            if(corec(k) < -1) corec(k) = -.5
        end do
        if(iter<=10) corec(1:npar_cycle) = corec(1:npar_cycle)/2
            write(IUNS,*)"Corrections"
            write(IUNS,'(20f11.3)') (corec(k), k=1,npar_cycle)
             ! UPDATE PARAMETERS

         99 write(IUNS,*)"Beta"
            write(IUNS,'(20f11.3)') (beta(k), k=1,p)
            if(RR*mls > 0) then
                write(IUNS,*)"Chol"
                write(IUNS,'(25g15.4)') (mychol(k),k=1,rr)
            end if
            if(nalpha > 0) then
                write(IUNS,*)"Alpha"
                write(IUNS,'(25g15.4)') (alpha(k),k=1,nalpha)
            end if
            write(IUNS,*)"Tau"
            write(IUNS,'(20f11.3)') (tau(k),k=1,s_cycle)
            if(ns_cycle > 0) then
                write(IUNS,*)"Spar"
                write(IUNS,'(20f11.3)') (spar(k),k=1,ns_cycle)
            end if

         WRITE(*,'("   -2 Log-Likelihood = ",F14.5)') -2*LOGL
            WRITE(IUNS,'("   -2 Log-Likelihood = ",F14.5)') -2*LOGL
!             WRITE(IUN,'("   -2 Log-Likelihood = ",F14.5)') -2*LOGL
            write(IUNS,*)"Corrections"
            write(IUNS,'(25f15.4)') (corec(k), k=1,npar_cycle)
!            write(*,*)"Corrections"
!            write(*,'(25f9.3)') (corec(k), k=1,npar_cycle)

            beta = beta + corec(1:p)
            if (s_cycle > 0) TAU(1:s_cycle)   = TAU(1:s_cycle) + COREC(nmeaneff+1:nmeaneff+S_cycle)
            if(RR*mls > 0 .and. iter > 14) then
                mychol = mychol + COREC(P+1:P+RR)/2
                kk = 1
                    do k=1, r
                        do LL=1, k
                            if(k==LL .and. mychol(kk) < 0) mychol(kk) = -mychol(kk)
                            kk = kk + 1
                        end do
                    end do
            else if(nalpha > 0) then
                alpha = alpha + COREC(P+1:P+nalpha)
            end if
            if(ns_cycle > 0 .and. (iter > 6 .or. (iter > 1 .and. cycles >= 5))) then
                do k=1,ns_cycle
                        spar(k) = spar(k) + corec(npar_cycle-ns_cycle+k)/2
!                    end if
                end do
                if(spar(ns_cycle) < 0 .and. ncov_cycle < 2) spar(ns_cycle) = abs(spar(ns_cycle))
                if(spar(1) < 0 .and. ncov_cycle == 0) spar(1) = abs(spar(1))
                if(ncov_cycle==2) then
                    if(spar(2) < 0) spar(2) = abs(spar(2))
                end if
                if(numrs > 1) then
                    if(ns_cycle .eq. numrs2) then
                        spar(1) = abs(spar(1))
                    else
                        spar(numloc+1) = abs(spar(numloc+1))
                    end if
                end if
            end if

            ITER = ITER+1
        END DO IFINLOOP
            if(logl < bestlogl-1) then
                WRITE(IUN,*) "-----------------------------------------------------------------------"
                WRITE(IUN,*) "WARNING: THIS MODEL IS LIKELY INACCURATE SINCE LOG LIKELIHOOD INCREASED!"
                WRITE(IUN,*) "-----------------------------------------------------------------------"
            else
                bestlogl = logl
            end if
!        sigma(1:rr) = mycholspar(1:rr)
        if(cycles .ne. 3 .or. ncov==0) then
            if(mls==0 .and. rold+rv==0 .and. chol<2) then
                mychol(1) = exp(alpha(1)/2)
                do k=1,npar_cycle
                    temp(p+1,k) = temp(p+1,k)*mychol(1)*.5
                    temp(k,p+1) = temp(k,p+1)*mychol(1)*.5
                end do
                adjvar = temp
            end if
            cholamt = rr
            cholcol = numloc
            mycholspar(1:rr) = mychol(1:rr)
!write(*,*) alpha,meanu,dot_product(alpha,meanu)/2
            sigma(1:rr) = mycholspar(1:rr)
            do j=1,myqdim2
                myorder(j) = j
                myorderinv(j) = j
            end do
            myorder2 = myorder
            if(ns_cycle > 0 .and. ncov_cycle .ne. 2) then 
                mycholspar(rr+1:rr+ns) = spar(1:ns)
                sigma(rr+1:rr+ns) = spar(1:ns)
                if(chol .ne. 1 .and. chol .ne. 2) then
                    cholamt = rr+ns
                    cholcol = myqdim
                    counter = 1
                    do j=0,ndim-1
                        do i=1,ndim-j
                            myorder(counter) = (j+i)*(j+i-1)/2 + j + 1
                            myorderinv(myorder(counter)) = counter
                            counter = counter + 1
                        end do
                    end do
                end if
            end if
            do j=1,myqdim2
                myorder2(j) = myorder(j)+p
                if(myorder(j)>rr) myorder2(j) = myorder2(j)+s
                myorderinv2(j) = myorderinv(j)+p
                if(myorder(j)>rr) myorderinv2(j) = myorderinv2(j)+s+nalpha-1
            end do
write(iuns,*) "Cholesky"
write(iuns,'(20f6.2)') (mycholspar(j),j=1,cholamt)
            do i=1,cholamt
                mycholspar2(i) = mycholspar(myorder(i))
            end do
write(iuns,*) "Ordering"
write(iuns,'(20i3)') (myorder(j),j=1,cholamt)
write(iuns,'(20i3)') (myorder2(j),j=1,cholamt)
write(iuns,'(20i3)') (myorderinv(j),j=1,cholamt)
write(iuns,'(20i3)') (myorderinv2(j),j=1,cholamt)

write(iuns,*) "Rearranged Cholesky"
write(iuns,'(20f6.2)') (mycholspar2(j),j=1,cholamt)
            if(chol .ne. 2) then
!    open(7,file="sstarinfo.out",access="append")
!                call getSStarRev(mycholspar2(1:cholamt),cholcol,cholamt,sstar(1:cholamt,1:cholamt))
                call getSStarRev2(mycholspar2(1:cholamt),cholcol,cholamt,sstar(1:cholamt,1:cholamt))
write(iuns,*) "Matrix to transform cholesky back"
do i=1,cholamt
    write(iuns,'(20f6.2)') (sstar(i,j),j=1,cholamt)
                end do
                do i=1,cholamt
                    sigma(i) = dot_product(mycholspar2(1:cholamt),sstar(i,1:cholamt))
                end do
!                sigma = matmul(sstar,mycholspar)
write(iuns,*) "Sigma"
write(iuns,'(20f6.2)') (sigma(j),j=1,cholamt)
write(iuns,*) "Rearranged Sigma"
write(iuns,'(20f6.2)') (sigma(myorderinv(j)),j=1,cholamt)
                asstar2 = 0
                do i=1,npar_cycle
                    asstar2(i,i) = 1
                end do
                ii=1
                do i=1,cholamt
                    do j=1,cholamt
                        asstar2(myorder2(i),myorder2(j)) = 2*sstar(i,j)
                    end do
                end do
write(iuns,*) "Matrix to transform var/covar"
do i=1,npar_cycle
    write(iuns,'(20f6.2)') (asstar2(i,j),j=1,npar_cycle)
end do
write(iuns,*) "Original var/covar values"
do i=1,npar_cycle
    write(iuns,'(20F12.4)') (temp(i,j), j=1,npar_cycle)
end do
                work(1:npar_cycle,1:npar_cycle) = matmul(asstar2(1:npar_cycle,1:npar_cycle), &
                                                            temp(1:npar_cycle,1:npar_cycle))        
                adjVar(1:npar_cycle,1:npar_cycle) = matmul(work(1:npar_cycle,1:npar_cycle),&
                                                transpose(asstar2(1:npar_cycle,1:npar_cycle)))
write(iuns,*) "Transformed var/covar values"
do i=1,npar_cycle
    write(iuns,'(20F12.4)') (adjvar(i,j), j=1,npar_cycle)
end do
            else
                sigma(1:rr) = mycholspar(1:rr)
                adjvar = temp
            end if

            DO k=1,npar_cycle
                se(k) = sqrt(abs(adjvar(k,k)))
            END DO
write(iuns,*) "Standard errors"
write(iuns,'(20F12.4)') (se(j), j=1,npar_cycle)
                
            if((cycles .ne. 3) .or. (cycles == 3 .and. ncov == 0)) then
                 ! WRITE RESULTS
               WRITE(IUN,562)ITER-1,ORIDGE,LOGL,LOGL-NPAR, &
               LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)),0-2*LOGL,0-2*(LOGL-NPAR), &
               0-2*(LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)))
               562 FORMAT(1X,'Total  Iterations =',I4,/, &
                          1X,'Final Ridge value =',F4.1,//, &
                     1X,'Log Likelihood                 = ',F12.3,/, &
                     1X,"Akaike's Information Criterion = ",F12.3,/, &
                     1X,"Schwarz's Bayesian Criterion   = ",F12.3,//,&
                     1X,"==> multiplied by -2             ",      /  &
                     1X,'Log Likelihood                 = ',F12.3,/, &
                     1X,"Akaike's Information Criterion = ",F12.3,/, &
                     1X,"Schwarz's Bayesian Criterion   = ",F12.3,/)
                WRITE(IUN,57)
 57 FORMAT(/,'Variable',12x,'    Estimate',4X,'AsymStdError',4x, &
          '     z-value',4X,'     p-value',/,'----------------',4x,  &
          '------------',4X,'------------',4X,'------------',4X,'------------')
            
            PVAL=0.0D0
            ZVAL=0.0D0
        
            if (NCENT==1 .AND. P>1) THEN
                write(IUN,'("STANDARDIZED BETA (regression coefficients)")')
            ELSE
                write(IUN,'("BETA (regression coefficients)")')
            end if
            DO L=1,P
                ZVAL = BETA(L)/SE(L)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                WRITE(IUN,804)BLAB(L),BETA(L),SE(L),ZVAL,PVAL
            end DO
            if(nalpha>0) then
                if (NCENT==1) THEN
                    write(IUN,'("STANDARDIZED ALPHA (BS variance parameters: log-linear model)")')
                 ELSE
                    write(IUN,'("ALPHA (BS variance parameters: log-linear model)")')
                end if
                 DO L=1,nalpha
                    L2 = P+L
                    myse = sqrt(temp(l2,l2))
                    ZVAL = ALPHA(L)/myse
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    WRITE(IUN,804)ALAB(L),ALPHA(L),myse,ZVAL,PVAL
                end DO
            else
                if(chol .ne. 2) then
                    WRITE(IUN,'("Random (location) Effect Variances and Covariances")')
                else
                    WRITE(IUN,'("Random (location) Effect Variances Cholesky terms")')
                end if
                counter = 1
                do i=1,r
                    do j=1,i
                        mycoef = sigma(myorderinv(counter))
                        myse = SE(counter+p)
                        zval = mycoef/myse
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        if(i==j) then
                            WRITE(IUN,804) alab(i),mycoef,myse,ZVAL,PVAL
                        else if(chol < 2) then
                            WRITE(IUN,806)"Covariance",i,j,mycoef,myse,ZVAL,PVAL
                        else
                            WRITE(IUN,805)"Cholesky",i,j,mycoef,myse,ZVAL,PVAL
                        end if
                        counter = counter+1
                    end do
                END DO
            end if
            if (NCENT==1 .AND. S>1) THEN 
                write(IUN,'("STANDARDIZED TAU (WS variance parameters: log-linear model)")')
            ELSE
                write(IUN,'("TAU (WS variance parameters: log-linear model)")')
            end if 
            DO L=1,S_cycle
                ZVAL = TAU(L)/SE(nmeaneff+L)
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                WRITE(IUN,804)TLAB(L),TAU(L),SE(nmeaneff+L),ZVAL,PVAL
            end DO
            
!                 IF (cycles==4 .or. (cycles==3 .and. ncov0==0)) THEN
            if(ns_cycle > 0) then
                if(ncov_cycle==2) then
                    WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT STANDARD DEVIATION")')
                    do k=1, 3
                        mycoef = spar(k)
                        myse = se(npar_cycle-3+k)
                        ZVAL = mycoef/myse
                        PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                        if(k==1) WRITE(IUN,804) 'Linear interact ', mycoef,myse,ZVAL,PVAL
                        if(k==2) WRITE(IUN,804) 'Std. Deviation  ', mycoef,myse,ZVAL,PVAL
                        if(k==3) WRITE(IUN,804) 'Quad. interact  ', mycoef,myse,ZVAL,PVAL
                    end do
                else
                    if(chol == 0) WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE COVARIANCE TERMS")')
                    if(chol > 0) WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE VARIANCE CHOLESKY TERMS")')
                    n=0
                    if(ncov_cycle == 0) then
                        do k=1, numrs
                            do m=1, k
                                n = n + 1
                                L2=nmeaneff+S_cycle+n
                                ZVAL = SPAR(n)/SE(L2)
                                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                                if(m .eq. k) then
                                    WRITE(IUN,804) rslabel(k),SPAR(n),SE(L2),ZVAL,PVAL
                                else
                                    WRITE(IUN,805)"Cholesky",k+numloc,m+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                                end if
                            end do
                        end do
                    else if(ncov_cycle == 1) then
                        do k=1, numrs
                            do m=1, k+numloc
                                n = n + 1
                                mycoef = sigma(myorderinv(n+numloc2))
                                myse = SE(nmeaneff+s_cycle+n)
                                ZVAL = mycoef/myse
                                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                                if(m .eq. k+numloc) then
                                    WRITE(IUN,804)rslabel(k),mycoef,myse,ZVAL,PVAL
                                else
                                    if(chol .eq. 0) then
                                        WRITE(IUN,806)"Covariance",k+numloc,m,mycoef,myse,ZVAL,PVAL
                                    else
                                        WRITE(IUN,805)"Cholesky",k+numloc,m,mycoef,myse,ZVAL,PVAL
                                    end if
                                end if
                            end do
                        end do
                    end if
                end if
            end if
            end if
     804 FORMAT(A16,4(4x,F12.5))
     805 FORMAT(A8,1x,i1,1x,i1,4x,4(4x,F12.5))
     806 FORMAT(A10,1x,i1,1x,i1,2x,4(4x,F12.5))
    
                 ! write out the deviance, estimates, and standard errors
            if (CYCLES.EQ.1) OPEN(2, FILE='mixREGLS_3level_.EST')
                WRITE(2,'(F15.6,3I8)') -2*LOGL,ITER-1,MAXIT, cycles
                WRITE(2,'(35F15.8)')(BETA(L),L=1,P)
                WRITE(2,'(35F15.8)')(alpha(L),L=1,nalpha)
                WRITE(2,'(35F15.8)')(sigma(myorderinv(L)),L=1,RR*mls)
                WRITE(2,'(35F15.8)')(TAU(L),L=1,S_cycle)
                if(ncov_cycle .eq. 1) then
                    write(2,'(35F15.8)')(sigma(myorderinv(numloc2+L)),L=1,ns_cycle)
                else
                    write(2,'(35F15.8)')(SPAR(L),L=1,ns_cycle)
                end if
                WRITE(2,'(35F15.8)')(SE(L),L=1,NPAR_cycle)
            close(IUN)
            
            OPEN(UNIT=IUN,FILE="mixREGLS_3level2.OUT",access="append")
        end if
    END DO CYCLELOOP
    write(iun,*)
    write(iun,*)
    myz = 1.959964
    if(mls .eq. 0) then
        WRITE(IUN,'("BS variance ratios and 95% CIs")')
        write(iun,'("------------------------------")')
        write(iun,*)
        WRITE(IUN,808) 'Variable        ','Ratio','Lower','Upper'
        write(iun,808)'---------------------','------------------','------------','------------'
        DO L=1,nalpha
            L2 = P+L
            tauhat = exp(alpha(l))
            tauhatlow = exp(alpha(l)-myz*se(l2))
            tauhatup = exp(alpha(l)+myz*se(l2))
            WRITE(IUN,804)aLAB(L),tauhat, tauhatlow, tauhatup
        end DO
         write(iun,*)
         write(iun,*)
     end if
808 FORMAT(A16,3(4x,A12))
     WRITE(IUN,'("WS variance ratios and 95% CIs")')
     write(iun,'("------------------------------")')
     write(iun,*)
    WRITE(IUN,808) 'Variable        ','Ratio','Lower','Upper'
    write(iun,808)'---------------------','------------------','------------','------------'

        write(IUN,'("TAU (WS variance parameters: exponentiated)")')
    DO L=1,S
        L2 = P+nalpha*(1-mls)+rr*mls+L
        tauhat = exp(tau(l))
        tauhatlow = exp(tau(l)-myz*se(l2))
        tauhatup = exp(tau(l)+myz*se(l2))
        WRITE(IUN,804)TLAB(L),tauhat, tauhatlow, tauhatup
    end DO
       
    CLOSE(IUN)
    CLOSE(IUNS)
    CLOSE(1)
    CLOSE(2)
    CLOSE(3)
    close(18)
        
    open(23,file=trim(fileprefix)//"_ebvar.dat")
    do k=1,nsubj
        write(23,'(i8,25g23.4)') idni(k,1),(thetas(k,i), i=1,ndim), ((thetavs(k,i,j),j=1,i),i=1,ndim)
    end do
    close(23)
        close(113)
        
    OPEN(UNIT=IUN,FILE="mixREGLS_3level_final.OUT")
        if(logl < bestlogl-1) then
            WRITE(IUN,*) "-----------------------------------------------------------------------"
            WRITE(IUN,*) "WARNING: THIS MODEL IS LIKELY INACCURATE SINCE LOG LIKELIHOOD INCREASED!"
            WRITE(IUN,*) "-----------------------------------------------------------------------"
            WRITE(IUN,*) 
        end if
        WRITE(IUN,*) 
        WRITE(IUN,*) 
        WRITE(IUN,*) "-------------"
        WRITE(IUN,*) "MODEL RESULTS"
        WRITE(IUN,*) "-------------"
        WRITE(IUN,*) 
        WRITE(IUN,562)ITER-1,ORIDGE,LOGL,LOGL-NPAR, LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)),0-2*LOGL,0-2*(LOGL-NPAR), &
           0-2*(LOGL-0.5D0*DBLE(NPAR)*DLOG(DBLE(nsubj)))
        WRITE(IUN,57)
        
        PVAL=0.0D0
        ZVAL=0.0D0
    
        if (NCENT==1 .AND. P>1) THEN
            write(IUN,'("STANDARDIZED BETA (regression coefficients)")')
        ELSE
            write(IUN,'("BETA (regression coefficients)")')
        end if
        DO L=1,P
            ZVAL = BETA(L)/SE(L)
            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
            WRITE(IUN,804)BLAB(L),BETA(L),SE(L),ZVAL,PVAL
        end DO
        if(nalpha>0) then
            if (NCENT==1) THEN
                write(IUN,'("STANDARDIZED ALPHA (BS variance parameters: log-linear model)")')
             ELSE
                write(IUN,'("ALPHA (BS variance parameters: log-linear model)")')
            end if
             DO L=1,nalpha
                L2 = P+L
                myse = sqrt(temp(l2,l2))
                ZVAL = ALPHA(L)/myse
                PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                WRITE(IUN,804)ALAB(L),ALPHA(L),myse,ZVAL,PVAL
            end DO
        else
            if(chol .ne. 2) then
                WRITE(IUN,'("Random (location) Effect Variances and Covariances")')
            else
                WRITE(IUN,'("Random (location) Effect Variances Cholesky terms")')
            end if
            counter = 1
            do i=1,r
                do j=1,i
                    mycoef = sigma(myorderinv(counter))
                    myse = SE(counter+p)
                    zval = mycoef/myse
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    if(i==j) then
                        WRITE(IUN,804) alab(i),mycoef,myse,ZVAL,PVAL
                    else if(chol < 2) then
                        WRITE(IUN,806)"Covariance",i,j,mycoef,myse,ZVAL,PVAL
                    else
                        WRITE(IUN,805)"Cholesky",i,j,mycoef,myse,ZVAL,PVAL
                    end if
                    counter = counter+1
                end do
            END DO
        end if
        if (NCENT==1 .AND. S>1) THEN 
            write(IUN,'("STANDARDIZED TAU (WS variance parameters: log-linear model)")')
        ELSE
            write(IUN,'("TAU (WS variance parameters: log-linear model)")')
        end if 
        DO L=1,S_cycle
            ZVAL = TAU(L)/SE(nmeaneff+L)
            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
            WRITE(IUN,804)TLAB(L),TAU(L),SE(nmeaneff+L),ZVAL,PVAL
        end DO
        
        if(ns_cycle > 0) then
            if(ncov_cycle==2) then
                WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE EFFECT STANDARD DEVIATION")')
                do k=1, 3
                    mycoef = spar(k)
                    myse = se(npar_cycle-3+k)
                    ZVAL = mycoef/myse
                    PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                    if(k==1) WRITE(IUN,804) 'Linear interact ', mycoef,myse,ZVAL,PVAL
                    if(k==2) WRITE(IUN,804) 'Std. Deviation  ', mycoef,myse,ZVAL,PVAL
                    if(k==3) WRITE(IUN,804) 'Quad. interact  ', mycoef,myse,ZVAL,PVAL
                end do
            else
                if(chol == 0) WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE COVARIANCE TERMS")')
                if(chol > 0) WRITE(IUN,'("SUBJECT-LEVEL RANDOM SCALE VARIANCE CHOLESKY TERMS")')
                n=0
                if(ncov_cycle == 0) then
                    do k=1, numrs
                        do m=1, k
                            n = n + 1
                            L2=nmeaneff+S_cycle+n
                            ZVAL = SPAR(n)/SE(L2)
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k) then
                                WRITE(IUN,804) rslabel(k),SPAR(n),SE(L2),ZVAL,PVAL
                            else
                                WRITE(IUN,805)"Cholesky",k+numloc,m+numloc,SPAR(n),SE(L2),ZVAL,PVAL
                            end if
                        end do
                    end do
                else if(ncov_cycle == 1) then
                    do k=1, numrs
                        do m=1, k+numloc
                            n = n + 1
                            mycoef = sigma(myorderinv(n+numloc2))
                            myse = SE(nmeaneff+s_cycle+n)
                            ZVAL = mycoef/myse
                            PVAL = 2.0D0 *(1.0D0 - PHIFN(ABS(ZVAL),0))
                            if(m .eq. k+numloc) then
                                WRITE(IUN,804)rslabel(k),mycoef,myse,ZVAL,PVAL
                            else
                                if(chol .eq. 0) then
                                    WRITE(IUN,806)"Covariance",k+numloc,m,mycoef,myse,ZVAL,PVAL
                                else
                                    WRITE(IUN,805)"Cholesky",k+numloc,m,mycoef,myse,ZVAL,PVAL
                                end if
                            end if
                        end do
                    end do
                end if
            end if
        end if
        write(iun,*)
    write(iun,*)
    if(mls .eq. 0) then
        WRITE(IUN,'("BS variance ratios and 95% CIs")')
        write(iun,'("------------------------------")')
        write(iun,*)
        WRITE(IUN,808) 'Variable        ','Ratio','Lower','Upper'
        write(iun,808)'---------------------','------------------','------------','------------'
        DO L=1,nalpha
            L2 = P+L
            tauhat = exp(alpha(l))
            tauhatlow = exp(alpha(l)-myz*se(l2))
            tauhatup = exp(alpha(l)+myz*se(l2))
            WRITE(IUN,804)aLAB(L),tauhat, tauhatlow, tauhatup
        end DO
         write(iun,*)
         write(iun,*)
     end if
     WRITE(IUN,'("WS variance ratios and 95% CIs")')
     write(iun,'("------------------------------")')
     write(iun,*)
    WRITE(IUN,808) 'Variable        ','Ratio','Lower','Upper'
    write(iun,808)'---------------------','------------------','------------','------------'

        write(IUN,'("TAU (WS variance parameters: exponentiated)")')
    DO L=1,S
        L2 = P+nalpha*(1-mls)+rr*mls+L
        tauhat = exp(tau(l))
        tauhatlow = exp(tau(l)-myz*se(l2))
        tauhatup = exp(tau(l)+myz*se(l2))
        WRITE(IUN,804)TLAB(L),tauhat, tauhatlow, tauhatup
    end DO

    close(IUN)

    open(237,file="mixregls_3level_.var")
    do k=1,npar
        write(237,'(25f18.6)') (adjVar(k,j), j=1,npar)
    end do
    close(237)
END SUBROUTINE estimate_level2